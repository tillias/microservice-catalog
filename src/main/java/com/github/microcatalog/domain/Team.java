package com.github.microcatalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

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

    @Column(name = "name")
    private String name;

    @Column(name = "it_product_owner")
    private String itProductOwner;

    @Column(name = "product_owner")
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

    public String getItProductOwner() {
        return itProductOwner;
    }

    public Team itProductOwner(String itProductOwner) {
        this.itProductOwner = itProductOwner;
        return this;
    }

    public void setItProductOwner(String itProductOwner) {
        this.itProductOwner = itProductOwner;
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
            ", itProductOwner='" + getItProductOwner() + "'" +
            ", productOwner='" + getProductOwner() + "'" +
            "}";
    }
}
