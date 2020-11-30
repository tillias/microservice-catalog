package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.custom.impact.analysis.Group;
import com.github.microcatalog.domain.custom.impact.analysis.Result;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class IntegrationTestsService {

    private final Logger log = LoggerFactory.getLogger(IntegrationTestsService.class);

    private final ImpactAnalysisService impactAnalysisService;
    private final MicroserviceRepository microserviceRepository;
    private final JenkinsService jenkinsService;

    public IntegrationTestsService(ImpactAnalysisService impactAnalysisService,
                                   MicroserviceRepository microserviceRepository,
                                   JenkinsService jenkinsService) {

        this.impactAnalysisService = impactAnalysisService;
        this.microserviceRepository = microserviceRepository;
        this.jenkinsService = jenkinsService;
    }

    public void triggerIntegrationTests(final Long microserviceId) {
        final Optional<Result> maybeResult = impactAnalysisService.calculate(microserviceId);
        if (!maybeResult.isPresent()) {
            return;
        }

        final Result impactAnalysisResult = maybeResult.get();
        impactAnalysisResult.getGroups().forEach(this::processGroup);
    }

    private void processGroup(final Group group) {
        group.getItems().forEach(i -> {
            final MicroserviceDto target = i.getTarget();
            final Long microserviceId = target.getId();
            final Optional<Microservice> maybeMicroservice = microserviceRepository.findById(microserviceId);
            if (maybeMicroservice.isPresent()) {
                final Microservice microservice = maybeMicroservice.get();
                jenkinsService.invokeJenkins(microservice);
            } else {
                log.warn("Can't find jenkinsUrl for microservice with id={}", microserviceId);
            }
        });
    }
}
