package com.github.microcatalog.service.dto.custom;

public class DependencyDto {
    private Long id;
    private String name;
    private MicroserviceDto source;
    private MicroserviceDto target;

    public DependencyDto id(Long id) {
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

    public MicroserviceDto getSource() {
        return source;
    }

    public void setSource(MicroserviceDto source) {
        this.source = source;
    }

    public MicroserviceDto getTarget() {
        return target;
    }

    public void setTarget(MicroserviceDto target) {
        this.target = target;
    }
}
