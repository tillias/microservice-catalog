package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.service.custom.MicroserviceService;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller which uses DTOs instead of JHipster Generated "domain" objects
 */
@RestController
@RequestMapping("/api")
public class MicroserviceCustomResource {
    private final MicroserviceService service;

    public MicroserviceCustomResource(MicroserviceService service) {
        this.service = service;
    }

    @GetMapping("/microservices/by/{ids}")
    public ResponseEntity<List<MicroserviceDto>> findByIds(@PathVariable List<Long> ids) {
        final List<MicroserviceDto> dependencies = service.findAllById(ids);
        return ResponseEntity.ok(dependencies);
    }
}
