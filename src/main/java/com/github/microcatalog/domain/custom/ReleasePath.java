package com.github.microcatalog.domain.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.microcatalog.domain.Microservice;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A ReleasePath.
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReleasePath {
    private Instant createdOn;
    private List<ReleaseGroup> groups = new ArrayList<>();
    private Microservice target;

    public Instant getCreatedOn() {
        return createdOn;
    }

    public ReleasePath createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public List<ReleaseGroup> getGroups() {
        return groups;
    }

    public ReleasePath groups(List<ReleaseGroup> releaseGroups) {
        this.groups = releaseGroups;
        return this;
    }

    public ReleasePath addGroups(ReleaseGroup releaseGroup) {
        this.groups.add(releaseGroup);
        return this;
    }

    public ReleasePath removeGroups(ReleaseGroup releaseGroup) {
        this.groups.remove(releaseGroup);
        return this;
    }

    public void setGroups(List<ReleaseGroup> releaseGroups) {
        this.groups = releaseGroups;
    }

    public Microservice getTarget() {
        return target;
    }

    public ReleasePath target(Microservice microservice) {
        this.target = microservice;
        return this;
    }

    public void setTarget(Microservice microservice) {
        this.target = microservice;
    }
}
