package com.github.microcatalog.service.dto.custom.builder;

import com.github.microcatalog.service.dto.custom.FullMicroserviceDto;
import com.github.microcatalog.service.dto.custom.StatusDto;
import com.github.microcatalog.service.dto.custom.TeamDto;

public final class FullMicroserviceDtoBuilder {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String swaggerUrl;
    private String gitUrl;
    private String ciUrl;
    private TeamDto team;
    private StatusDto status;

    private FullMicroserviceDtoBuilder() {
    }

    public static FullMicroserviceDtoBuilder aFullMicroserviceDto() {
        return new FullMicroserviceDtoBuilder();
    }

    public FullMicroserviceDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public FullMicroserviceDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FullMicroserviceDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public FullMicroserviceDtoBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public FullMicroserviceDtoBuilder withSwaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
        return this;
    }

    public FullMicroserviceDtoBuilder withGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
        return this;
    }

    public FullMicroserviceDtoBuilder withCiUrl(String ciUrl) {
        this.ciUrl = ciUrl;
        return this;
    }

    public FullMicroserviceDtoBuilder withTeam(TeamDto team) {
        this.team = team;
        return this;
    }

    public FullMicroserviceDtoBuilder withStatus(StatusDto status) {
        this.status = status;
        return this;
    }

    public FullMicroserviceDto build() {
        FullMicroserviceDto fullMicroserviceDto = new FullMicroserviceDto();
        fullMicroserviceDto.setId(id);
        fullMicroserviceDto.setName(name);
        fullMicroserviceDto.setDescription(description);
        fullMicroserviceDto.setImageUrl(imageUrl);
        fullMicroserviceDto.setSwaggerUrl(swaggerUrl);
        fullMicroserviceDto.setGitUrl(gitUrl);
        fullMicroserviceDto.setCiUrl(ciUrl);
        fullMicroserviceDto.setTeam(team);
        fullMicroserviceDto.setStatus(status);
        return fullMicroserviceDto;
    }
}
