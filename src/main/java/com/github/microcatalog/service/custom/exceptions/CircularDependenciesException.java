package com.github.microcatalog.service.custom.exceptions;

import com.github.microcatalog.domain.Microservice;

import java.util.Set;

/**
 * Occurs when adding new dependency will introduce cycle
 */
public class CircularDependenciesException extends RuntimeException {
    private final Set<Microservice> cycles;

    public CircularDependenciesException(String message, Set<Microservice> cycles) {
        super(message);

        this.cycles = cycles;
    }

    public Set<Microservice> getCycles() {
        return cycles;
    }
}
