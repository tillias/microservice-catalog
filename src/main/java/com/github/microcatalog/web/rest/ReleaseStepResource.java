package com.github.microcatalog.web.rest;

import com.github.microcatalog.domain.ReleaseStep;
import com.github.microcatalog.repository.ReleaseStepRepository;
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
 * REST controller for managing {@link com.github.microcatalog.domain.ReleaseStep}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReleaseStepResource {

    private final Logger log = LoggerFactory.getLogger(ReleaseStepResource.class);

    private static final String ENTITY_NAME = "releaseStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleaseStepRepository releaseStepRepository;

    public ReleaseStepResource(ReleaseStepRepository releaseStepRepository) {
        this.releaseStepRepository = releaseStepRepository;
    }

    /**
     * {@code POST  /release-steps} : Create a new releaseStep.
     *
     * @param releaseStep the releaseStep to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releaseStep, or with status {@code 400 (Bad Request)} if the releaseStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/release-steps")
    public ResponseEntity<ReleaseStep> createReleaseStep(@Valid @RequestBody ReleaseStep releaseStep) throws URISyntaxException {
        log.debug("REST request to save ReleaseStep : {}", releaseStep);
        if (releaseStep.getId() != null) {
            throw new BadRequestAlertException("A new releaseStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleaseStep result = releaseStepRepository.save(releaseStep);
        return ResponseEntity.created(new URI("/api/release-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /release-steps} : Updates an existing releaseStep.
     *
     * @param releaseStep the releaseStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releaseStep,
     * or with status {@code 400 (Bad Request)} if the releaseStep is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releaseStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/release-steps")
    public ResponseEntity<ReleaseStep> updateReleaseStep(@Valid @RequestBody ReleaseStep releaseStep) throws URISyntaxException {
        log.debug("REST request to update ReleaseStep : {}", releaseStep);
        if (releaseStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReleaseStep result = releaseStepRepository.save(releaseStep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releaseStep.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /release-steps} : get all the releaseSteps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releaseSteps in body.
     */
    @GetMapping("/release-steps")
    public List<ReleaseStep> getAllReleaseSteps() {
        log.debug("REST request to get all ReleaseSteps");
        return releaseStepRepository.findAll();
    }

    /**
     * {@code GET  /release-steps/:id} : get the "id" releaseStep.
     *
     * @param id the id of the releaseStep to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releaseStep, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/release-steps/{id}")
    public ResponseEntity<ReleaseStep> getReleaseStep(@PathVariable Long id) {
        log.debug("REST request to get ReleaseStep : {}", id);
        Optional<ReleaseStep> releaseStep = releaseStepRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(releaseStep);
    }

    /**
     * {@code DELETE  /release-steps/:id} : delete the "id" releaseStep.
     *
     * @param id the id of the releaseStep to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/release-steps/{id}")
    public ResponseEntity<Void> deleteReleaseStep(@PathVariable Long id) {
        log.debug("REST request to delete ReleaseStep : {}", id);
        releaseStepRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
