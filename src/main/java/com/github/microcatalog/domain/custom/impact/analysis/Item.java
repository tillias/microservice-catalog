package com.github.microcatalog.domain.custom.impact.analysis;

import com.github.microcatalog.domain.Microservice;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item {
    private Microservice target;
    private List<Microservice> siblings;

    public Microservice getTarget() {
        return target;
    }

    public Item target(Microservice microservice) {
        this.target = microservice;
        return this;
    }

    public void setTarget(Microservice microservice) {
        this.target = microservice;
    }

    public List<Microservice> getSiblings() {
        return siblings;
    }

    public Item siblings(List<Microservice> siblings) {
        this.siblings = siblings;
        return this;
    }

    public void setSiblings(List<Microservice> siblings) {
        this.siblings = siblings;
    }
}
