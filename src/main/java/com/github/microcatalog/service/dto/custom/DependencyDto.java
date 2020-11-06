package com.github.microcatalog.service.dto.custom;

public class DependencyDto extends BaseDto {
    private MicroserviceDto source;
    private MicroserviceDto target;

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
