package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.service.custom.DependencyService;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller which uses DTOs instead of JHipster Generated "domain" objects
 */
@RestController
@RequestMapping("/api")
public class DependencyCustomResource {

    private final DependencyService service;

    public DependencyCustomResource(DependencyService service) {
        this.service = service;
    }

    @GetMapping("/dependencies/by/{ids}")
    public ResponseEntity<List<DependencyDto>> findByIds(@PathVariable List<Long> ids) {
        final List<DependencyDto> dependencies = service.findAllById(ids);
        return ResponseEntity.ok(dependencies);
    }

    @DeleteMapping("/dependencies/by/name/{name}")
    public ResponseEntity<Long> deleteByName(@PathVariable String name) {
        return ResponseEntity.ok(service.deleteByName(name));
    }
}
