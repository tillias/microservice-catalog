package com.github.microcatalog.service.custom;

import com.github.microcatalog.config.ApplicationProperties;
import com.github.microcatalog.service.dto.custom.JenkinsCrumbDto;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;
import java.net.URL;

@Service
public class JenkinsService {
    private final Logger log = LoggerFactory.getLogger(JenkinsService.class);

    private final WebClient webClient;

    public JenkinsService(ApplicationProperties applicationProperties) throws SSLException {
        final ApplicationProperties.IntegrationTests.Jenkins jenkins
            = applicationProperties.getIntegrationTests().getJenkins();

        SslContext sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();
        TcpClient tcpClient = TcpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        HttpClient httpClient = HttpClient.from(tcpClient);
        this.webClient = WebClient.builder()
            .defaultHeaders(header -> header.setBasicAuth(jenkins.getUser(), jenkins.getToken()))
            .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    public void invokeJenkins(final String ciUrl) {
        if (StringUtils.isBlank(ciUrl)) {
            throw new IllegalArgumentException("Microservice CI url can't be blank");
        }

        try {
            String microserviceUrl = ciUrl;

            // See https://stackoverflow.com/questions/48993367/jenkins-is-returning-302-to-bitbucker-webhook
            if (!microserviceUrl.endsWith("/")) {
                microserviceUrl += "/";
            }

            final URL url = new URL(microserviceUrl);
            final URL crumbIssuerUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), "/crumbIssuer/api/json");

            final JenkinsCrumbDto jenkinsCrumb = webClient
                .get()
                .uri(crumbIssuerUrl.toString())
                .retrieve()
                .bodyToMono(JenkinsCrumbDto.class).block();

            if (jenkinsCrumb != null) {
                webClient.post().uri(microserviceUrl + "build")
                    .header(jenkinsCrumb.getCrumbRequestField(), jenkinsCrumb.getCrumb())
                    .exchange()
                    .block();
            } else {
                log.error("Error getting jenkins crumb for {}", ciUrl);
            }
        } catch (Exception ex) {
            log.error("Error triggering jenkins build for {}", ciUrl, ex);
        }
    }
}
