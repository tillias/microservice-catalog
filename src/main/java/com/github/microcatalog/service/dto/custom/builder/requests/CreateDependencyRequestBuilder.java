package com.github.microcatalog.service.dto.custom.builder.requests;

import com.github.microcatalog.service.dto.custom.requests.CreateDependencyRequest;

public final class CreateDependencyRequestBuilder {
    private String dependencyName;
    private String sourceName;
    private String targetName;

    private CreateDependencyRequestBuilder() {
    }

    public static CreateDependencyRequestBuilder aCreateDependencyRequest() {
        return new CreateDependencyRequestBuilder();
    }

    public CreateDependencyRequestBuilder withDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
        return this;
    }

    public CreateDependencyRequestBuilder withSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public CreateDependencyRequestBuilder withTargetName(String targetName) {
        this.targetName = targetName;
        return this;
    }

    public CreateDependencyRequest build() {
        CreateDependencyRequest createDependencyRequest = new CreateDependencyRequest();
        createDependencyRequest.setDependencyName(dependencyName);
        createDependencyRequest.setSourceName(sourceName);
        createDependencyRequest.setTargetName(targetName);
        return createDependencyRequest;
    }
}
