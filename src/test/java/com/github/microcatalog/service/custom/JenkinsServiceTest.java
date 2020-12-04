package com.github.microcatalog.service.custom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9999)
class JenkinsServiceTest {

    @Autowired
    private JenkinsService sut;

    @Test
    void invokeJenkins_NullMicroservice_ExceptionIsThrown() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            sut.invokeJenkins(null)
        );
    }

    @Test
    void invokeJenkins_MicroserviceWithoutCiUrl_ExceptionIsThrown() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            sut.invokeJenkins(null)
        );
    }

    @Test
    void invokeJenkins_ValidMicroservice_Success() {
        stubFor(get(urlEqualTo("/crumbIssuer/api/json"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"crumb\":\"aabbccddeeff\",\"crumbRequestField\":\"Jenkins-Crumb\"}"))
        );

        sut.invokeJenkins("http://localhost:9999/job/Service21/");

        verify(postRequestedFor(urlEqualTo("/job/Service21/build"))
            .withHeader("Jenkins-Crumb", equalTo("aabbccddeeff"))
        );
    }
}
