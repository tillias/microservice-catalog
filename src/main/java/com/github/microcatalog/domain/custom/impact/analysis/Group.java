package com.github.microcatalog.domain.custom.impact.analysis;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group {
    private Set<Item> items = new HashSet<>();

    public Set<Item> getItems() {
        return items;
    }

    public Group items(Set<Item> items) {
        this.items = items;
        return this;
    }

    public void addItem(final Item item) {
        if (item == null) {
            return;
        }

        this.items.add(item);
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Optional<Item> findByTargetId(final long targetId) {
        return items.stream()
            .filter(i -> Objects.equals(targetId, i.getTarget().getId()))
            .findAny();
    }
}
