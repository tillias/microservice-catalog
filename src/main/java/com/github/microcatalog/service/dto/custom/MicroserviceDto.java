package com.github.microcatalog.service.dto.custom;

public class MicroserviceDto {
    private Long id;
    private String name;

    public MicroserviceDto id(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
