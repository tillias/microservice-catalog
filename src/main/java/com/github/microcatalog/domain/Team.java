package com.github.microcatalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "team_lead", nullable = false)
    private String teamLead;

    @NotNull
    @Column(name = "product_owner", nullable = false)
    private String productOwner;

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

    public Team name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamLead() {
        return teamLead;
    }

    public Team teamLead(String teamLead) {
        this.teamLead = teamLead;
        return this;
    }

    public void setTeamLead(String teamLead) {
        this.teamLead = teamLead;
    }

    public String getProductOwner() {
        return productOwner;
    }

    public Team productOwner(String productOwner) {
        this.productOwner = productOwner;
        return this;
    }

    public void setProductOwner(String productOwner) {
        this.productOwner = productOwner;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        return id != null && id.equals(((Team) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", teamLead='" + getTeamLead() + "'" +
            ", productOwner='" + getProductOwner() + "'" +
            "}";
    }
}
