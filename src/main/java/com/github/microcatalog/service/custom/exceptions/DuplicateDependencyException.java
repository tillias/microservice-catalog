package com.github.microcatalog.service.custom.exceptions;

import com.github.microcatalog.domain.Dependency;

public class DuplicateDependencyException extends RuntimeException {
    private final Dependency dependency;

    public DuplicateDependencyException(String message, Dependency dependency) {
        super(message);
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }
}
