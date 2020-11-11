package com.github.microcatalog.domain.custom.impact.analysis;

import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item {
    private MicroserviceDto target;
    private List<MicroserviceDto> siblings;

    public MicroserviceDto getTarget() {
        return target;
    }

    public Item target(MicroserviceDto microservice) {
        this.target = microservice;
        return this;
    }

    public void setTarget(MicroserviceDto microservice) {
        this.target = microservice;
    }

    public List<MicroserviceDto> getSiblings() {
        return siblings;
    }

    public Item siblings(List<MicroserviceDto> siblings) {
        this.siblings = siblings;
        return this;
    }

    public void setSiblings(List<MicroserviceDto> siblings) {
        this.siblings = siblings;
    }
}
