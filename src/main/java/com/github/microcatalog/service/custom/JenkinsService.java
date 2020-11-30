package com.github.microcatalog.service.custom;

import com.github.microcatalog.config.ApplicationProperties;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.service.dto.custom.JenkinsCrumbDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;

@Service
public class JenkinsService {
    private final Logger log = LoggerFactory.getLogger(JenkinsService.class);

    private final WebClient webClient;

    public JenkinsService(ApplicationProperties applicationProperties,
                          WebClient.Builder webClientBuilder) {
        final ApplicationProperties.IntegrationTests.Jenkins jenkins
            = applicationProperties.getIntegrationTests().getJenkins();
        this.webClient = webClientBuilder
            .defaultHeaders(header -> header.setBasicAuth(jenkins.getUser(), jenkins.getToken()))
            .build();
    }

    public void invokeJenkins(final Microservice microservice) {
        try {
            String microserviceUrl = microservice.getCiUrl();

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

                webClient
                    .post()
                    .uri(String.format("%s/build", microservice.getCiUrl()))
                    .exchange()
                    .block();
            } else {
                log.error("Error getting jenkins crumb for microservice {}", microservice.getId());
            }
        } catch (Exception ex) {
            log.error("Error triggering jenkins build for microservice {}", microservice.getId(), ex);
        }
    }
}
