package com.github.microcatalog.service.dto.custom.builder;

import com.github.microcatalog.service.dto.custom.DependencyDto;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;

public final class DependencyDtoBuilder {
    private MicroserviceDto source;
    private MicroserviceDto target;
    private Long id;
    private String name;

    private DependencyDtoBuilder() {
    }

    public static DependencyDtoBuilder aDependencyDto() {
        return new DependencyDtoBuilder();
    }

    public DependencyDtoBuilder withSource(MicroserviceDto source) {
        this.source = source;
        return this;
    }

    public DependencyDtoBuilder withTarget(MicroserviceDto target) {
        this.target = target;
        return this;
    }

    public DependencyDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DependencyDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DependencyDto build() {
        DependencyDto dependencyDto = new DependencyDto();
        dependencyDto.setSource(source);
        dependencyDto.setTarget(target);
        dependencyDto.setId(id);
        dependencyDto.setName(name);
        return dependencyDto;
    }
}
