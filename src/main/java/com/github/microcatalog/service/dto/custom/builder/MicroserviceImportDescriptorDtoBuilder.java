package com.github.microcatalog.service.dto.custom.builder;

import com.github.microcatalog.service.dto.custom.MicroserviceImportDescriptorDto;

import java.util.List;

public final class MicroserviceImportDescriptorDtoBuilder {
    private String name;
    private String description;
    private String imageUrl;
    private String apiUrl;
    private String ciUrl;
    private String gitUrl;
    private String team;
    private String status;
    private List<MicroserviceImportDescriptorDto> dependencies;

    private MicroserviceImportDescriptorDtoBuilder() {
    }

    public static MicroserviceImportDescriptorDtoBuilder aMicroserviceImportDescriptorDto() {
        return new MicroserviceImportDescriptorDtoBuilder();
    }

    public MicroserviceImportDescriptorDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withCiUrl(String ciUrl) {
        this.ciUrl = ciUrl;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withTeam(String team) {
        this.team = team;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public MicroserviceImportDescriptorDtoBuilder withDependencies(List<MicroserviceImportDescriptorDto> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    public MicroserviceImportDescriptorDto build() {
        MicroserviceImportDescriptorDto microserviceImportDescriptorDto = new MicroserviceImportDescriptorDto();
        microserviceImportDescriptorDto.setName(name);
        microserviceImportDescriptorDto.setDescription(description);
        microserviceImportDescriptorDto.setImageUrl(imageUrl);
        microserviceImportDescriptorDto.setApiUrl(apiUrl);
        microserviceImportDescriptorDto.setCiUrl(ciUrl);
        microserviceImportDescriptorDto.setGitUrl(gitUrl);
        microserviceImportDescriptorDto.setTeam(team);
        microserviceImportDescriptorDto.setStatus(status);
        microserviceImportDescriptorDto.setDependencies(dependencies);
        return microserviceImportDescriptorDto;
    }
}
