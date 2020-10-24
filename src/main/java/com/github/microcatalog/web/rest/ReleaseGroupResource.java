package com.github.microcatalog.web.rest;

import com.github.microcatalog.domain.ReleaseGroup;
import com.github.microcatalog.repository.ReleaseGroupRepository;
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
 * REST controller for managing {@link com.github.microcatalog.domain.ReleaseGroup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReleaseGroupResource {

    private final Logger log = LoggerFactory.getLogger(ReleaseGroupResource.class);

    private static final String ENTITY_NAME = "releaseGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleaseGroupRepository releaseGroupRepository;

    public ReleaseGroupResource(ReleaseGroupRepository releaseGroupRepository) {
        this.releaseGroupRepository = releaseGroupRepository;
    }

    /**
     * {@code POST  /release-groups} : Create a new releaseGroup.
     *
     * @param releaseGroup the releaseGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releaseGroup, or with status {@code 400 (Bad Request)} if the releaseGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/release-groups")
    public ResponseEntity<ReleaseGroup> createReleaseGroup(@Valid @RequestBody ReleaseGroup releaseGroup) throws URISyntaxException {
        log.debug("REST request to save ReleaseGroup : {}", releaseGroup);
        if (releaseGroup.getId() != null) {
            throw new BadRequestAlertException("A new releaseGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleaseGroup result = releaseGroupRepository.save(releaseGroup);
        return ResponseEntity.created(new URI("/api/release-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /release-groups} : Updates an existing releaseGroup.
     *
     * @param releaseGroup the releaseGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releaseGroup,
     * or with status {@code 400 (Bad Request)} if the releaseGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releaseGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/release-groups")
    public ResponseEntity<ReleaseGroup> updateReleaseGroup(@Valid @RequestBody ReleaseGroup releaseGroup) throws URISyntaxException {
        log.debug("REST request to update ReleaseGroup : {}", releaseGroup);
        if (releaseGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReleaseGroup result = releaseGroupRepository.save(releaseGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releaseGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /release-groups} : get all the releaseGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releaseGroups in body.
     */
    @GetMapping("/release-groups")
    public List<ReleaseGroup> getAllReleaseGroups() {
        log.debug("REST request to get all ReleaseGroups");
        return releaseGroupRepository.findAll();
    }

    /**
     * {@code GET  /release-groups/:id} : get the "id" releaseGroup.
     *
     * @param id the id of the releaseGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releaseGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/release-groups/{id}")
    public ResponseEntity<ReleaseGroup> getReleaseGroup(@PathVariable Long id) {
        log.debug("REST request to get ReleaseGroup : {}", id);
        Optional<ReleaseGroup> releaseGroup = releaseGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(releaseGroup);
    }

    /**
     * {@code DELETE  /release-groups/:id} : delete the "id" releaseGroup.
     *
     * @param id the id of the releaseGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/release-groups/{id}")
    public ResponseEntity<Void> deleteReleaseGroup(@PathVariable Long id) {
        log.debug("REST request to delete ReleaseGroup : {}", id);
        releaseGroupRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
