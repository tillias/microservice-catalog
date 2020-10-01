package com.github.microcatalog.web.rest;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.MicroserviceRepository;
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
 * REST controller for managing {@link com.github.microcatalog.domain.Microservice}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MicroserviceResource {

    private final Logger log = LoggerFactory.getLogger(MicroserviceResource.class);

    private static final String ENTITY_NAME = "microservice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MicroserviceRepository microserviceRepository;

    public MicroserviceResource(MicroserviceRepository microserviceRepository) {
        this.microserviceRepository = microserviceRepository;
    }

    /**
     * {@code POST  /microservices} : Create a new microservice.
     *
     * @param microservice the microservice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new microservice, or with status {@code 400 (Bad Request)} if the microservice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/microservices")
    public ResponseEntity<Microservice> createMicroservice(@Valid @RequestBody Microservice microservice) throws URISyntaxException {
        log.debug("REST request to save Microservice : {}", microservice);
        if (microservice.getId() != null) {
            throw new BadRequestAlertException("A new microservice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Microservice result = microserviceRepository.save(microservice);
        return ResponseEntity.created(new URI("/api/microservices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /microservices} : Updates an existing microservice.
     *
     * @param microservice the microservice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated microservice,
     * or with status {@code 400 (Bad Request)} if the microservice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the microservice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/microservices")
    public ResponseEntity<Microservice> updateMicroservice(@Valid @RequestBody Microservice microservice) throws URISyntaxException {
        log.debug("REST request to update Microservice : {}", microservice);
        if (microservice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Microservice result = microserviceRepository.save(microservice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, microservice.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /microservices} : get all the microservices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of microservices in body.
     */
    @GetMapping("/microservices")
    public List<Microservice> getAllMicroservices() {
        log.debug("REST request to get all Microservices");
        return microserviceRepository.findAll();
    }

    /**
     * {@code GET  /microservices/:id} : get the "id" microservice.
     *
     * @param id the id of the microservice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the microservice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/microservices/{id}")
    public ResponseEntity<Microservice> getMicroservice(@PathVariable Long id) {
        log.debug("REST request to get Microservice : {}", id);
        Optional<Microservice> microservice = microserviceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(microservice);
    }

    /**
     * {@code DELETE  /microservices/:id} : delete the "id" microservice.
     *
     * @param id the id of the microservice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/microservices/{id}")
    public ResponseEntity<Void> deleteMicroservice(@PathVariable Long id) {
        log.debug("REST request to delete Microservice : {}", id);
        microserviceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
