package com.github.microcatalog.utils;

import com.github.microcatalog.domain.Dependency;

public class DependencyBuilder {
    private Long id;
    private String name;
    private Long source;
    private Long target;

    public DependencyBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DependencyBuilder withName(String name) {
        this.name = name;
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
        result.setName(name);
        result.setId(id);
        result.setSource(new MicroserviceBuilder().withId(source).build());
        result.setTarget(new MicroserviceBuilder().withId(target).build());

        return result;
    }
}
