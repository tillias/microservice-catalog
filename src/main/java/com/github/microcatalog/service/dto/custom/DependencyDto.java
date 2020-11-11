package com.github.microcatalog.service.dto.custom;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class DependencyDto extends BaseDto implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseDto baseDto = (BaseDto) o;

        return new EqualsBuilder()
            .append(getId(), baseDto.getId())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getId())
            .toHashCode();
    }
}
