package com.github.microcatalog.domain.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.microcatalog.domain.Microservice;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A ReleaseStep.
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReleaseStep {
    private Microservice workItem;
    private List<Microservice> parentWorkItems;

    public Microservice getWorkItem() {
        return workItem;
    }

    public ReleaseStep workItem(Microservice microservice) {
        this.workItem = microservice;
        return this;
    }

    public void setWorkItem(Microservice microservice) {
        this.workItem = microservice;
    }

    public List<Microservice> getParentWorkItems() {
        return parentWorkItems;
    }

    public ReleaseStep parentWorkItems(List<Microservice> microservices) {
        this.parentWorkItems = microservices;
        return this;
    }

    public void setParentWorkItems(List<Microservice> parentWorkItems) {
        this.parentWorkItems = parentWorkItems;
    }
}
