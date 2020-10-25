package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.domain.custom.ReleasePath;
import com.github.microcatalog.service.custom.ReleasePathCustomService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST controller for calculation of release paths {@link ReleasePath}.
 */
@RestController
@RequestMapping("/api")
public class ReleasePathCustomResource {

    private final Logger log = LoggerFactory.getLogger(ReleasePathCustomResource.class);

    private final ReleasePathCustomService service;

    public ReleasePathCustomResource(ReleasePathCustomService service) {
        this.service = service;
    }

    /**
     * {@code GET  /release-path/microservice/:microserviceId} : get the "microserviceId" releasePath.
     *
     * @param microserviceId id of Microservice for which releasePath should be calculated
     * @return release path calculated for given microservice
     */
    @GetMapping("/release-path/microservice/{microserviceId}")
    public ResponseEntity<ReleasePath> getPath(@PathVariable Long microserviceId) {
        log.debug("REST request to get ReleasePath for microserviceId : {}", microserviceId);
        final Optional<ReleasePath> releasePath = service.getReleasePath(microserviceId);
        return ResponseUtil.wrapOrNotFound(releasePath);
    }
}
