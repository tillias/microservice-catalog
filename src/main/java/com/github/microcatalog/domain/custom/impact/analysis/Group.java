package com.github.microcatalog.domain.custom.impact.analysis;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.*;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group {
    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public Group items(List<Item> items) {
        this.items = items;
        return this;
    }

    public void addItem(final Item item) {
        if (item == null) {
            return;
        }

        this.items.add(item);
    }

    public void sortItemsById() {
        items.sort(Comparator.comparing(a -> a.getTarget().getId()));
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Optional<Item> findByTargetId(final long targetId) {
        return items.stream()
            .filter(i -> Objects.equals(targetId, i.getTarget().getId()))
            .findAny();
    }
}
