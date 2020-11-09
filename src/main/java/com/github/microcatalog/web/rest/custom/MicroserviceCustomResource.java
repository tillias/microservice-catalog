package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.service.custom.MicroserviceService;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/microservices/by/name/{name}")
    public ResponseEntity<Long> deleteByName(@PathVariable String name) {
        return ResponseEntity.ok(service.deleteByName(name));
    }
}
