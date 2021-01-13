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
    private final ApplicationProperties.Imports imports = new Imports();

    public IntegrationTests getIntegrationTests() {
        return integrationTests;
    }

    public Imports getImports() {
        return imports;
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

    public static class Imports {
        private final Imports.Defaults defaults = new Defaults();

        public Defaults getDefaults() {
            return defaults;
        }

        public static class Defaults {
            private String description;
            private String imageUrl;
            private String apiUrl;
            private String gitUrl;
            private String ciUrl;
            private String status;
            private final Defaults.Team team = new Team();


            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getApiUrl() {
                return apiUrl;
            }

            public void setApiUrl(String apiUrl) {
                this.apiUrl = apiUrl;
            }

            public String getGitUrl() {
                return gitUrl;
            }

            public void setGitUrl(String gitUrl) {
                this.gitUrl = gitUrl;
            }

            public String getCiUrl() {
                return ciUrl;
            }

            public void setCiUrl(String ciUrl) {
                this.ciUrl = ciUrl;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Team getTeam() {
                return team;
            }

            public static class Team {
                private String name;
                private String po;
                private String tl;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPo() {
                    return po;
                }

                public void setPo(String po) {
                    this.po = po;
                }

                public String getTl() {
                    return tl;
                }

                public void setTl(String tl) {
                    this.tl = tl;
                }
            }
        }
    }
}
