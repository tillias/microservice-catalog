package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.custom.ReleaseGroup;
import com.github.microcatalog.domain.custom.ReleasePath;
import com.github.microcatalog.domain.custom.ReleaseStep;
import com.github.microcatalog.service.GraphUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {ReleasePathCustomService.class})
class ReleasePathCustomServiceTest {

    @MockBean
    private GraphLoaderService graphLoaderService;

    @Autowired
    private ReleasePathCustomService service;

    @Test
    void getReleasePath_NodeOutsideGraph_EmptyPath() {
        given(graphLoaderService.loadGraph())
            .willReturn(
                GraphUtils.createGraph(String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3;",
                    "1 -> 2;",
                    "2 -> 3;}"
                    )
                )
            );

        Optional<ReleasePath> maybePath = service.getReleasePath(4L);
        assertThat(maybePath).isEmpty();
    }

    @Test
    void getReleasePath_NoCycles_Success() {
        given(graphLoaderService.loadGraph())
            .willReturn(
                GraphUtils.createGraph(String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3; 4; 5; 6; 7; 8; 9; 10; 11; 12;",
                    "1 -> 2;",
                    "2 -> 4;",
                    "6 -> 4;",
                    "4 -> 5;",
                    "4 -> 7;",
                    "4 -> 8;",
                    "3 -> 9;",
                    "3 -> 11;",
                    "12 -> 1;",
                    "7 -> 5;",
                    "10 -> 1;}"
                    )
                )
            );

        Optional<ReleasePath> maybePath = service.getReleasePath(1L);
        assertThat(maybePath).isPresent();

        ReleasePath path = maybePath.get();

        ReleaseStep step = getStep(path, 0, 5L);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(4L, 7L);

        step = getStep(path, 0, 8L);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(4L);

        step = getStep(path, 1, 7L);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(4L);

        step = getStep(path, 2, 4L);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(2L);

        step = getStep(path, 3, 2L);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(1L);

        step = getStep(path, 4, 1L);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).isEmpty();
    }

    @Test
    void getReleasePath_ContainsCyclesInSameComponent_ExceptionIsThrown() {
        given(graphLoaderService.loadGraph())
            .willReturn(
                GraphUtils.createGraph(String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3; 5; 6; 7;",
                    "1 -> 2;",
                    "2 -> 3;",
                    "3 -> 1;",
                    "5 -> 6;",
                    "5 -> 7;}"
                    )
                )
            );

        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                service.getReleasePath(1L)
            )
            .withMessageStartingWith("There are cyclic dependencies between microservices");
    }

    @Test
    void getReleasePath_ContainsCyclesInOtherComponent_Success() {
        given(graphLoaderService.loadGraph())
            .willReturn(
                GraphUtils.createGraph(String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3; 4; 5; 6; 7; 8;",
                    "1 -> 2;",
                    "2 -> 3;",
                    "2 -> 4;",
                    "5 -> 6;",
                    "6 -> 7;",
                    "7 -> 8;",
                    "8 -> 6;}"
                    )
                )
            );

        Optional<ReleasePath> maybePath = service.getReleasePath(1L);
        assertThat(maybePath).isPresent();

        ReleasePath path = maybePath.get();

        ReleaseStep step = getStep(path, 0, 3);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(2L);

        step = getStep(path, 0, 4);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(2L);

        step = getStep(path, 1, 2);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).extracting(Microservice::getId).containsExactlyInAnyOrder(1L);

        step = getStep(path, 2, 1L);
        assertThat(step).isNotNull();
        assertThat(step.getParentWorkItems()).isEmpty();
    }

    private ReleaseStep getStep(final ReleasePath path, int groupIndex, long microserviceId) {
        final Set<ReleaseStep> steps = getSteps(path, groupIndex);
        if (steps != null) {
            Optional<ReleaseStep> maybeStep = steps.stream()
                .filter(s -> s.getWorkItem() != null && microserviceId == s.getWorkItem().getId()).findFirst();
            if (maybeStep.isPresent()) {
                return maybeStep.get();
            }
        }

        return null;
    }

    private Set<ReleaseStep> getSteps(ReleasePath path, int groupIndex) {
        if (path.getGroups() == null) {
            return null;
        }

        if (groupIndex > path.getGroups().size()) {
            return null;
        }

        ReleaseGroup first = path.getGroups().get(groupIndex);
        if (first != null) {
            return first.getSteps();
        } else {
            return Collections.emptySet();
        }
    }
}
