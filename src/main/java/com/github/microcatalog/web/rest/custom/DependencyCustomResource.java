package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.service.custom.DependencyService;
import com.github.microcatalog.service.dto.custom.DependencyDto;
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
}
