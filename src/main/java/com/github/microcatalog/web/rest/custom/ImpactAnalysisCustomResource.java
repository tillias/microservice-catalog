package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.domain.custom.impact.analysis.Result;
import com.github.microcatalog.service.custom.ImpactAnalysisService;
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
 * REST controller for calculation of impact analysis {@link Result}
 */
@RestController
@RequestMapping("/api")
public class ImpactAnalysisCustomResource {

    private final Logger log = LoggerFactory.getLogger(ImpactAnalysisCustomResource.class);

    private final ImpactAnalysisService service;

    public ImpactAnalysisCustomResource(ImpactAnalysisService service) {
        this.service = service;
    }

    /**
     * {@code GET  /impact-analysis/microservice/:microserviceId} : get the impact analysis for "microserviceId"
     *
     * @param microserviceId id of Microservice for which impact analysis should be performed
     * @return impact analysis calculated for given microservice
     */
    @GetMapping("/impact-analysis/microservice/{microserviceId}")
    public ResponseEntity<Result> calculate(@PathVariable Long microserviceId) {
        log.debug("REST request to get impact analysis Result for microserviceId : {}", microserviceId);
        final Optional<Result> impactAnalysisResult = service.calculate(microserviceId);
        return ResponseUtil.wrapOrNotFound(impactAnalysisResult);
    }
}
