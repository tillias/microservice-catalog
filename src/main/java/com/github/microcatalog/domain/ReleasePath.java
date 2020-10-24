package com.github.microcatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ReleasePath.
 */
@Entity
@Table(name = "release_path")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReleasePath implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_on")
    private Instant createdOn;

    @OneToMany(mappedBy = "releasePath")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ReleaseGroup> groups = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "releasePaths", allowSetters = true)
    private Microservice target;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<ReleaseGroup> getGroups() {
        return groups;
    }

    public ReleasePath groups(Set<ReleaseGroup> releaseGroups) {
        this.groups = releaseGroups;
        return this;
    }

    public ReleasePath addGroups(ReleaseGroup releaseGroup) {
        this.groups.add(releaseGroup);
        releaseGroup.setReleasePath(this);
        return this;
    }

    public ReleasePath removeGroups(ReleaseGroup releaseGroup) {
        this.groups.remove(releaseGroup);
        releaseGroup.setReleasePath(null);
        return this;
    }

    public void setGroups(Set<ReleaseGroup> releaseGroups) {
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleasePath)) {
            return false;
        }
        return id != null && id.equals(((ReleasePath) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleasePath{" +
            "id=" + getId() +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
