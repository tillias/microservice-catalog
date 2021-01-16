package com.github.microcatalog.service.dto.custom;

import java.util.List;

public final class MicroserviceImportDescriptorDto {
    private String name;
    private String description;
    private String imageUrl;
    private String apiUrl;
    private String ciUrl;
    private String gitUrl;
    private String team;
    private String status;
    private List<MicroserviceImportDescriptorDto> dependencies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getCiUrl() {
        return ciUrl;
    }

    public void setCiUrl(String ciUrl) {
        this.ciUrl = ciUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MicroserviceImportDescriptorDto> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<MicroserviceImportDescriptorDto> dependencies) {
        this.dependencies = dependencies;
    }
}
