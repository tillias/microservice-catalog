package com.github.microcatalog.utils;

import com.github.microcatalog.domain.Microservice;

public class MicroserviceBuilder {
    private Long id;
    private String name;

    public MicroserviceBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MicroserviceBuilder withName(String name){
        this.name = name;
        return this;
    }

    public Microservice build() {
        final Microservice result = new Microservice();
        result.setId(id);
        result.setName(name);
        return result;
    }
}
