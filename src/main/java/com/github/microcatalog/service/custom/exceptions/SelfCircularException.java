package com.github.microcatalog.service.custom.exceptions;

/**
 * Occurs when there is attempt creating dependency with source and target pointing to the same microservice
 */
public class SelfCircularException extends RuntimeException {
    public SelfCircularException(String message) {
        super(message);
    }
}
