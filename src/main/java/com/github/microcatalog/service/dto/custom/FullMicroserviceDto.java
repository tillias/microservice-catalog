package com.github.microcatalog.service.dto.custom;

import java.io.Serializable;

public final class FullMicroserviceDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -7994184124965371580L;

    private String description;
    private String imageUrl;
    private String swaggerUrl;
    private String gitUrl;
    private String ciUrl;
    private TeamDto team;
    private StatusDto status;

    public String getCiUrl() {
        return ciUrl;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getSwaggerUrl() {
        return swaggerUrl;
    }

    public void setSwaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public void setCiUrl(String ciUrl) {
        this.ciUrl = ciUrl;
    }

    public TeamDto getTeam() {
        return team;
    }

    public void setTeam(TeamDto team) {
        this.team = team;
    }

    public StatusDto getStatus() {
        return status;
    }

    public void setStatus(StatusDto status) {
        this.status = status;
    }
}
