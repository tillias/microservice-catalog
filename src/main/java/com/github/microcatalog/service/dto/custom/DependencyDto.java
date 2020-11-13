package com.github.microcatalog.service.dto.custom;

import java.io.Serializable;

public final class DependencyDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 984616324705586469L;

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
