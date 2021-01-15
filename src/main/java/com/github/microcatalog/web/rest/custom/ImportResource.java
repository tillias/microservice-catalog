package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.service.custom.ImportService;
import com.github.microcatalog.service.dto.custom.FullMicroserviceDto;
import com.github.microcatalog.service.dto.custom.MicroserviceImportDescriptorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for importing {@link com.github.microcatalog.domain.Microservice} with {@link com.github.microcatalog.domain.Dependency}
 */
@RestController
@RequestMapping("/api")
public class ImportResource {
    private final ImportService importService;

    public ImportResource(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping
    public ResponseEntity<FullMicroserviceDto> importMicroservice(@RequestBody MicroserviceImportDescriptorDto descriptor) {
        return ResponseEntity.of(importService.importFromDescriptor(descriptor));
    }
}
