package com.github.microcatalog.service.dto.custom;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class MicroserviceDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 4624953286552443974L;

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
