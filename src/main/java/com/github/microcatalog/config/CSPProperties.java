package com.github.microcatalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "csp", ignoreUnknownFields = false)
public class CSPProperties {
    private String imageSrc = "'self' data:";

    public CSPProperties() {
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
