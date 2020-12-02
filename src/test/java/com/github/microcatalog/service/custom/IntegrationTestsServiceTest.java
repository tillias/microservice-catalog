package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.custom.impact.analysis.Group;
import com.github.microcatalog.domain.custom.impact.analysis.Item;
import com.github.microcatalog.domain.custom.impact.analysis.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.github.microcatalog.service.dto.custom.builder.MicroserviceDtoBuilder.aMicroserviceDto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest(classes = {IntegrationTestsService.class})
class IntegrationTestsServiceTest {

    @Autowired
    private IntegrationTestsService sut;

    @MockBean
    private ImpactAnalysisService impactAnalysisService;

    @MockBean
    private JenkinsService jenkinsService;

    @Test
    void triggerIntegrationTests_MicroserviceNotFound_NoException() {
        given(impactAnalysisService.calculate(eq(1L))).willReturn(Optional.empty());
        assertFalse(sut.triggerIntegrationTests(1L));

        then(impactAnalysisService).should().calculate(1L);
    }

    @Test
    void triggerIntegrationTests_ValidMicroservice_Success() {
        given(impactAnalysisService.calculate(eq(1L)))
            .willReturn(
                Optional.of(
                    new Result().groups(Arrays.asList(
                        new Group().items(Collections.singletonList(
                            new Item().target(aMicroserviceDto().withCiUrl("http://foo.com/job/first").build()))),
                        new Group().items(Arrays.asList(
                            new Item().target(aMicroserviceDto().withCiUrl("http://foo.com/job/second").build()),
                            new Item().target(aMicroserviceDto().withCiUrl("http://foo.com/job/third").build())
                        )))
                    )
                )
            );
        sut.triggerIntegrationTests(1L);

        then(jenkinsService).should().invokeJenkins("http://foo.com/job/first");
        then(jenkinsService).should().invokeJenkins("http://foo.com/job/second");
        then(jenkinsService).should().invokeJenkins("http://foo.com/job/third");
    }
}
