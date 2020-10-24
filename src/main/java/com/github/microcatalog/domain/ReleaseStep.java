package com.github.microcatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ReleaseStep.
 */
@Entity
@Table(name = "release_step")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReleaseStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "releaseSteps", allowSetters = true)
    private Microservice workItem;

    @ManyToOne
    @JsonIgnoreProperties(value = "steps", allowSetters = true)
    private ReleaseGroup releaseGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ReleaseGroup getReleaseGroup() {
        return releaseGroup;
    }

    public ReleaseStep releaseGroup(ReleaseGroup releaseGroup) {
        this.releaseGroup = releaseGroup;
        return this;
    }

    public void setReleaseGroup(ReleaseGroup releaseGroup) {
        this.releaseGroup = releaseGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleaseStep)) {
            return false;
        }
        return id != null && id.equals(((ReleaseStep) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleaseStep{" +
            "id=" + getId() +
            "}";
    }
}
