package com.github.microcatalog.service.dto.custom.builder;

import com.github.microcatalog.service.dto.custom.MicroserviceDto;

public final class MicroserviceDtoBuilder {
    private Long id;
    private String name;

    private MicroserviceDtoBuilder() {
    }

    public static MicroserviceDtoBuilder aMicroserviceDto() {
        return new MicroserviceDtoBuilder();
    }

    public MicroserviceDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MicroserviceDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MicroserviceDto build() {
        MicroserviceDto microserviceDto = new MicroserviceDto();
        microserviceDto.setId(id);
        microserviceDto.setName(name);
        return microserviceDto;
    }
}
