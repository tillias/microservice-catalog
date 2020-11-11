package com.github.microcatalog.service.custom.exceptions;

import com.github.microcatalog.service.dto.custom.MicroserviceDto;

import java.util.Set;

/**
 * Occurs when adding new dependency will introduce cycle
 */
public class CircularDependenciesException extends RuntimeException {
    private static final long serialVersionUID = -8709192334943830989L;

    private final Set<MicroserviceDto> cycles;

    public CircularDependenciesException(String message, Set<MicroserviceDto> cycles) {
        super(message);

        this.cycles = cycles;
    }

    public Set<MicroserviceDto> getCycles() {
        return cycles;
    }
}
