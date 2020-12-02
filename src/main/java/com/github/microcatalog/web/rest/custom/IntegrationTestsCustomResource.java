package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.service.custom.IntegrationTestsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for triggering integraion tests
 */
@RestController
@RequestMapping("/api")
public class IntegrationTestsCustomResource {
    private final IntegrationTestsService service;

    public IntegrationTestsCustomResource(IntegrationTestsService service) {
        this.service = service;
    }

    @PostMapping("integration-tests/microservice/{microserviceId}")
    public void triggerIntegrationTests(@PathVariable Long microserviceId) {
        service.triggerIntegrationTests(microserviceId);
    }
}
