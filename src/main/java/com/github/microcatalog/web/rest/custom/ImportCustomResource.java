package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.security.AuthoritiesConstants;
import com.github.microcatalog.service.custom.ImportService;
import com.github.microcatalog.service.dto.custom.FullMicroserviceDto;
import com.github.microcatalog.service.dto.custom.MicroserviceImportDescriptorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for importing {@link com.github.microcatalog.domain.Microservice} and creating it's dependencies
 */
@RestController
@RequestMapping("/api")
public class ImportCustomResource {
    private final ImportService importService;

    public ImportCustomResource(ImportService importService) {
        this.importService = importService;
    }

    /**
     * {@code POST  /import : import microservices with it's dependencies
     *
     * @param descriptor describes microservice to be imported and it's dependent microservices
     * @return microservice (without dependencies) if created
     */
    @PostMapping("/import")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.IMPORT + "\")")
    public ResponseEntity<FullMicroserviceDto> importMicroservice(@RequestBody MicroserviceImportDescriptorDto descriptor) {
        return ResponseEntity.of(importService.importFromDescriptor(descriptor));
    }
}
