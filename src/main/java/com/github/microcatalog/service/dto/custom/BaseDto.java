package com.github.microcatalog.service.dto.custom;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class BaseDto {
    private Long id;
    private String name;

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseDto baseDto = (BaseDto) o;

        return new EqualsBuilder()
            .append(getId(), baseDto.getId())
            .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getId())
            .toHashCode();
    }
}
