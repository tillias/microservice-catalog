package com.github.microcatalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Microcatalog.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final ApplicationProperties.IntegrationTests integrationTests = new IntegrationTests();

    public IntegrationTests getIntegrationTests() {
        return integrationTests;
    }

    public static class IntegrationTests {

        private final IntegrationTests.Jenkins jenkins = new Jenkins();

        public Jenkins getJenkins() {
            return jenkins;
        }

        public static class Jenkins {
            private String user;
            private String token;
            private boolean disableSSL;
            private String crumbIssuer;

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public boolean isDisableSSL() {
                return disableSSL;
            }

            public void setDisableSSL(boolean disableSSL) {
                this.disableSSL = disableSSL;
            }

            public String getCrumbIssuer() {
                return crumbIssuer;
            }

            public void setCrumbIssuer(String crumbIssuer) {
                this.crumbIssuer = crumbIssuer;
            }
        }
    }
}
