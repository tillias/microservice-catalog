package com.github.microcatalog.utils;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;

public class DependencyBuilder {
    private Long id;
    private Long source;
    private Long target;

    public DependencyBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DependencyBuilder withSource(Long sourceId) {
        this.source = sourceId;
        return this;
    }

    public DependencyBuilder withTarget(Long targetId) {
        this.target = targetId;
        return this;
    }

    public Dependency build() {
        final Dependency result = new Dependency();

        result.setId(id);
        result.setSource(new MicroserviceBuilder().withId(source).build());
        result.setTarget(new MicroserviceBuilder().withId(target).build());

        return result;
    }
}