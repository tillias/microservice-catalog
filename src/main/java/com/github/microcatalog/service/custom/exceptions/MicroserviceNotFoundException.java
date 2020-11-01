package com.github.microcatalog.service.custom.exceptions;

public class MicroserviceNotFoundException extends RuntimeException {
    private final long microserviceId;

    public MicroserviceNotFoundException(String message, long microserviceId) {
        super(message);

        this.microserviceId = microserviceId;
    }

    public long getMicroserviceId() {
        return microserviceId;
    }
}
