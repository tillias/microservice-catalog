package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.service.custom.DependencyService;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller which uses DTOs instead of domain objects
 */
@RestController
@RequestMapping("/api")
public class DependencyCustomResource {
    private final Logger log = LoggerFactory.getLogger(DependencyCustomResource.class);

    private final DependencyService service;

    public DependencyCustomResource(DependencyService service) {
        log.debug("{} is initialized", getClass());
        this.service = service;
    }

    @GetMapping("/dependencies/by/{ids}")
    public ResponseEntity<List<DependencyDto>> findByIds(@PathVariable List<Long> ids) {
        final List<DependencyDto> dependencies = service.findAllById(ids);
        return ResponseEntity.ok(dependencies);
    }
}
