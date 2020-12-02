package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.custom.impact.analysis.Group;
import com.github.microcatalog.domain.custom.impact.analysis.Result;
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
    private final JenkinsService jenkinsService;

    public IntegrationTestsService(ImpactAnalysisService impactAnalysisService,
                                   JenkinsService jenkinsService) {

        this.impactAnalysisService = impactAnalysisService;
        this.jenkinsService = jenkinsService;
    }

    public boolean triggerIntegrationTests(final Long microserviceId) {
        final Optional<Result> maybeResult = impactAnalysisService.calculate(microserviceId);
        if (!maybeResult.isPresent()) {
            return false;
        }

        final Result impactAnalysisResult = maybeResult.get();
        impactAnalysisResult.getGroups().forEach(this::processGroup);

        return true;
    }

    private void processGroup(final Group group) {
        group.getItems().forEach(i -> {
            final MicroserviceDto target = i.getTarget();
            if (target != null) {
                jenkinsService.invokeJenkins(target.getCiUrl());
            } else {
                log.warn("Error processing group item. Target is null");
            }
        });
    }
}
