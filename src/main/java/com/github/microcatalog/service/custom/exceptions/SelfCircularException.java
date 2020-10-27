package com.github.microcatalog.service.custom.exceptions;

import com.github.microcatalog.domain.Microservice;

/**
 * Occurs when there is attempt creating dependency with source and target pointing to the same microservice
 */
public class SelfCircularException extends RuntimeException {
    private final Microservice source;

    public SelfCircularException(String message, Microservice source) {
        super(message);

        this.source = source;
    }

    public Microservice getSource() {
        return source;
    }
}
