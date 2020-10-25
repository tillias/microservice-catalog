package com.github.microcatalog.utils;

import com.github.microcatalog.domain.Microservice;

public class MicroserviceBuilder {
    private Long id;

    public MicroserviceBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public Microservice build() {
        final Microservice result = new Microservice();
        result.setId(id);
        return result;
    }
}
