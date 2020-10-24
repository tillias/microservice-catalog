package com.github.microcatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ReleaseGroup.
 */
@Entity
@Table(name = "release_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReleaseGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "releaseGroup")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ReleaseStep> steps = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "groups", allowSetters = true)
    private ReleasePath releasePath;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ReleaseStep> getSteps() {
        return steps;
    }

    public ReleaseGroup steps(Set<ReleaseStep> releaseSteps) {
        this.steps = releaseSteps;
        return this;
    }

    public ReleaseGroup addSteps(ReleaseStep releaseStep) {
        this.steps.add(releaseStep);
        releaseStep.setReleaseGroup(this);
        return this;
    }

    public ReleaseGroup removeSteps(ReleaseStep releaseStep) {
        this.steps.remove(releaseStep);
        releaseStep.setReleaseGroup(null);
        return this;
    }

    public void setSteps(Set<ReleaseStep> releaseSteps) {
        this.steps = releaseSteps;
    }

    public ReleasePath getReleasePath() {
        return releasePath;
    }

    public ReleaseGroup releasePath(ReleasePath releasePath) {
        this.releasePath = releasePath;
        return this;
    }

    public void setReleasePath(ReleasePath releasePath) {
        this.releasePath = releasePath;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleaseGroup)) {
            return false;
        }
        return id != null && id.equals(((ReleaseGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleaseGroup{" +
            "id=" + getId() +
            "}";
    }
}
