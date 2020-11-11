package com.github.microcatalog.domain.custom;

import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

/**
 * A ReleaseStep.
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReleaseStep {
    private MicroserviceDto workItem;
    private List<MicroserviceDto> parentWorkItems;

    public MicroserviceDto getWorkItem() {
        return workItem;
    }

    public ReleaseStep workItem(MicroserviceDto microservice) {
        this.workItem = microservice;
        return this;
    }

    public void setWorkItem(MicroserviceDto microservice) {
        this.workItem = microservice;
    }

    public List<MicroserviceDto> getParentWorkItems() {
        return parentWorkItems;
    }

    public ReleaseStep parentWorkItems(List<MicroserviceDto> microservices) {
        this.parentWorkItems = microservices;
        return this;
    }

    public void setParentWorkItems(List<MicroserviceDto> parentWorkItems) {
        this.parentWorkItems = parentWorkItems;
    }
}
