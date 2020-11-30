package com.github.microcatalog.service.dto.custom;

public class JenkinsCrumbDto {
    private String crumbRequestField;
    private String crumb;

    public String getCrumbRequestField() {
        return crumbRequestField;
    }

    public void setCrumbRequestField(String crumbRequestField) {
        this.crumbRequestField = crumbRequestField;
    }

    public String getCrumb() {
        return crumb;
    }

    public void setCrumb(String crumb) {
        this.crumb = crumb;
    }
}
