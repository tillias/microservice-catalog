package com.github.microcatalog.service.custom.exceptions;

/**
 * Used by {@link com.github.microcatalog.service.custom.ImportService}
 */
public class ImportException extends RuntimeException {
    private static final long serialVersionUID = -5092030802613208627L;

    public ImportException(String message) {
        super(message);
    }
}
