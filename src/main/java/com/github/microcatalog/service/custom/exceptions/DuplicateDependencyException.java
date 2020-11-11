package com.github.microcatalog.service.custom.exceptions;

import com.github.microcatalog.service.dto.custom.DependencyDto;

/**
 * Occurs when dependency with given source and target already exists
 */
public class DuplicateDependencyException extends RuntimeException {
    private static final long serialVersionUID = -6123761206700419670L;

    private final DependencyDto dependency;

    public DuplicateDependencyException(String message, DependencyDto dependency) {
        super(message);
        this.dependency = dependency;
    }

    public DependencyDto getDependency() {
        return dependency;
    }
}
