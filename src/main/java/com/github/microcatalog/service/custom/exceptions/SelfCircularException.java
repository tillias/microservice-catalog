package com.github.microcatalog.service.custom.exceptions;

import com.github.microcatalog.service.dto.custom.MicroserviceDto;

/**
 * Occurs when there is attempt creating dependency with source and target pointing to the same microservice
 */
public class SelfCircularException extends RuntimeException {
    private static final long serialVersionUID = 8533101069660472099L;

    private final MicroserviceDto source;

    public SelfCircularException(String message, MicroserviceDto source) {
        super(message);

        this.source = source;
    }

    public MicroserviceDto getSource() {
        return source;
    }
}
