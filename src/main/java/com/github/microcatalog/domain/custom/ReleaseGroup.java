package com.github.microcatalog.domain.custom;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.*;

/**
 * A ReleaseGroup.
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReleaseGroup {
    private List<ReleaseStep> steps = new ArrayList<>();

    public List<ReleaseStep> getSteps() {
        return steps;
    }

    public ReleaseGroup steps(List<ReleaseStep> releaseSteps) {
        this.steps = releaseSteps;
        return this;
    }

    public ReleaseGroup addSteps(ReleaseStep releaseStep) {
        this.steps.add(releaseStep);
        return this;
    }

    public ReleaseGroup removeSteps(ReleaseStep releaseStep) {
        this.steps.remove(releaseStep);
        return this;
    }

    public void setSteps(List<ReleaseStep> releaseSteps) {
        this.steps = releaseSteps;
    }

    public Optional<ReleaseStep> findByTargetId(final long targetId) {
        return steps.stream()
            .filter(i -> Objects.equals(targetId, i.getWorkItem().getId()))
            .findAny();
    }

    public void sortStepsById() {
        steps.sort(Comparator.comparing(a -> a.getWorkItem().getId()));
    }
}
