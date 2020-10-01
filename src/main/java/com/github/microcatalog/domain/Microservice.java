package com.github.microcatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Microservice.
 */
@Entity
@Table(name = "microservice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Microservice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "swagger_url")
    private String swaggerUrl;

    @Column(name = "git_url")
    private String gitUrl;

    @ManyToOne
    @JsonIgnoreProperties(value = "microservices", allowSetters = true)
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Microservice name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Microservice description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Microservice imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSwaggerUrl() {
        return swaggerUrl;
    }

    public Microservice swaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
        return this;
    }

    public void setSwaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public Microservice gitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
        return this;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public Team getTeam() {
        return team;
    }

    public Microservice team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Microservice)) {
            return false;
        }
        return id != null && id.equals(((Microservice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Microservice{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", swaggerUrl='" + getSwaggerUrl() + "'" +
            ", gitUrl='" + getGitUrl() + "'" +
            "}";
    }
}
