package com.github.microcatalog.web.rest;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.service.custom.DependencyService;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.github.microcatalog.domain.Dependency}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DependencyResource {

    private final Logger log = LoggerFactory.getLogger(DependencyResource.class);

    private static final String ENTITY_NAME = "dependency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DependencyService dependencyService;

    public DependencyResource(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    /**
     * {@code POST  /dependencies} : Create a new dependency.
     *
     * @param dependency the dependency to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dependency, or with status {@code 400 (Bad Request)} if the dependency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws SelfCircularException if source and target of dependency is the same microservice
     */
    @PostMapping("/dependencies")
    public ResponseEntity<Dependency> createDependency(@Valid @RequestBody Dependency dependency) throws URISyntaxException {
        log.debug("REST request to save Dependency : {}", dependency);

        Dependency result = dependencyService.create(dependency);
        return ResponseEntity.created(new URI("/api/dependencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dependencies} : Updates an existing dependency.
     *
     * @param dependency the dependency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependency,
     * or with status {@code 400 (Bad Request)} if the dependency is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dependency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dependencies")
    public ResponseEntity<Dependency> updateDependency(@Valid @RequestBody Dependency dependency) throws URISyntaxException {
        log.debug("REST request to update Dependency : {}", dependency);

        Dependency result = dependencyService.update(dependency);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependency.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dependencies} : get all the dependencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dependencies in body.
     */
    @GetMapping("/dependencies")
    public List<Dependency> getAllDependencies() {
        log.debug("REST request to get all Dependencies");
        return dependencyService.findAll();
    }

    /**
     * {@code GET  /dependencies/:id} : get the "id" dependency.
     *
     * @param id the id of the dependency to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dependency, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dependencies/{id}")
    public ResponseEntity<Dependency> getDependency(@PathVariable Long id) {
        log.debug("REST request to get Dependency : {}", id);
        Optional<Dependency> dependency = dependencyService.findById(id);
        return ResponseUtil.wrapOrNotFound(dependency);
    }

    /**
     * {@code DELETE  /dependencies/:id} : delete the "id" dependency.
     *
     * @param id the id of the dependency to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dependencies/{id}")
    public ResponseEntity<Void> deleteDependency(@PathVariable Long id) {
        log.debug("REST request to delete Dependency : {}", id);
        dependencyService.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
