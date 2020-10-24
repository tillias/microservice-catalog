package com.github.microcatalog.web.rest;

import com.github.microcatalog.domain.ReleasePath;
import com.github.microcatalog.repository.ReleasePathRepository;
import com.github.microcatalog.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.github.microcatalog.domain.ReleasePath}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReleasePathResource {

    private final Logger log = LoggerFactory.getLogger(ReleasePathResource.class);

    private static final String ENTITY_NAME = "releasePath";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleasePathRepository releasePathRepository;

    public ReleasePathResource(ReleasePathRepository releasePathRepository) {
        this.releasePathRepository = releasePathRepository;
    }

    /**
     * {@code POST  /release-paths} : Create a new releasePath.
     *
     * @param releasePath the releasePath to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releasePath, or with status {@code 400 (Bad Request)} if the releasePath has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/release-paths")
    public ResponseEntity<ReleasePath> createReleasePath(@Valid @RequestBody ReleasePath releasePath) throws URISyntaxException {
        log.debug("REST request to save ReleasePath : {}", releasePath);
        if (releasePath.getId() != null) {
            throw new BadRequestAlertException("A new releasePath cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleasePath result = releasePathRepository.save(releasePath);
        return ResponseEntity.created(new URI("/api/release-paths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /release-paths} : Updates an existing releasePath.
     *
     * @param releasePath the releasePath to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releasePath,
     * or with status {@code 400 (Bad Request)} if the releasePath is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releasePath couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/release-paths")
    public ResponseEntity<ReleasePath> updateReleasePath(@Valid @RequestBody ReleasePath releasePath) throws URISyntaxException {
        log.debug("REST request to update ReleasePath : {}", releasePath);
        if (releasePath.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReleasePath result = releasePathRepository.save(releasePath);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releasePath.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /release-paths} : get all the releasePaths.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releasePaths in body.
     */
    @GetMapping("/release-paths")
    public List<ReleasePath> getAllReleasePaths() {
        log.debug("REST request to get all ReleasePaths");
        return releasePathRepository.findAll();
    }

    /**
     * {@code GET  /release-paths/:id} : get the "id" releasePath.
     *
     * @param id the id of the releasePath to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releasePath, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/release-paths/{id}")
    public ResponseEntity<ReleasePath> getReleasePath(@PathVariable Long id) {
        log.debug("REST request to get ReleasePath : {}", id);
        Optional<ReleasePath> releasePath = releasePathRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(releasePath);
    }

    /**
     * {@code DELETE  /release-paths/:id} : delete the "id" releasePath.
     *
     * @param id the id of the releasePath to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/release-paths/{id}")
    public ResponseEntity<Void> deleteReleasePath(@PathVariable Long id) {
        log.debug("REST request to delete ReleasePath : {}", id);
        releasePathRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
