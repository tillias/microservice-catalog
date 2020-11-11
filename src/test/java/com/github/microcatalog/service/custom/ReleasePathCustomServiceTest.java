package com.github.microcatalog.service.custom;

import com.github.microcatalog.MappersConfig;
import com.github.microcatalog.domain.custom.ReleaseGroup;
import com.github.microcatalog.domain.custom.ReleasePath;
import com.github.microcatalog.domain.custom.ReleaseStep;
import com.github.microcatalog.service.GraphUtils;
import com.github.microcatalog.service.custom.exceptions.MicroserviceNotFoundException;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {ReleasePathCustomService.class, MappersConfig.class})
class ReleasePathCustomServiceTest {

    @MockBean
    private GraphLoaderService graphLoaderService;

    @Autowired
    private ReleasePathCustomService sut;

    @Test
    void calculate_EmptyGraph_EmptyResult() {
        given(graphLoaderService.loadGraph())
            .willReturn(new DefaultDirectedGraph<>(DefaultEdge.class));

        Optional<ReleasePath> path = sut.getReleasePath(4L);
        assertThat(path).isNotNull().isNotPresent();
    }

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

        assertThatThrownBy(() -> sut.getReleasePath(4L))
            .isInstanceOf(MicroserviceNotFoundException.class)
            .hasMessageStartingWith("Microservice not found")
            .asInstanceOf(InstanceOfAssertFactories.type(MicroserviceNotFoundException.class))
            .extracting(MicroserviceNotFoundException::getMicroserviceId)
            .isEqualTo(4L);
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

        Optional<ReleasePath> maybePath = sut.getReleasePath(1L);
        assertThat(maybePath).isPresent();

        final ReleasePath path = maybePath.get();

        final List<ReleaseGroup> groups = path.getGroups();

        assertThat(groups).isNotEmpty().hasSize(5);

        ReleaseGroup group = groups.get(0);
        assertThat(group.getSteps()).isNotEmpty().hasSize(2);
        ReleaseStep step = group.findByTargetId(5L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(4L, 7L);
        step = group.findByTargetId(8L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(4L);

        group = groups.get(1);
        assertThat(group.getSteps()).isNotEmpty().hasSize(1);
        step = group.findByTargetId(7L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(4L);

        group = groups.get(2);
        assertThat(group.getSteps()).isNotEmpty().hasSize(1);
        step = group.findByTargetId(4L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(2L);

        group = groups.get(3);
        assertThat(group.getSteps()).isNotEmpty().hasSize(1);
        step = group.findByTargetId(2L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(1L);

        group = groups.get(4);
        assertThat(group.getSteps()).isNotEmpty().hasSize(1);
        step = group.findByTargetId(1L).orElseThrow(NoSuchElementException::new);
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
                sut.getReleasePath(1L)
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

        Optional<ReleasePath> maybePath = sut.getReleasePath(1L);
        assertThat(maybePath).isPresent();

        final ReleasePath path = maybePath.get();
        final List<ReleaseGroup> groups = path.getGroups();

        assertThat(groups).isNotEmpty().hasSize(3);

        ReleaseGroup group = groups.get(0);
        assertThat(group.getSteps()).isNotEmpty().hasSize(2);
        ReleaseStep step = group.findByTargetId(3L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(2L);
        step = group.findByTargetId(4L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(2L);

        group = groups.get(1);
        assertThat(group.getSteps()).isNotEmpty().hasSize(1);
        step = group.findByTargetId(2L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).extracting(MicroserviceDto::getId).containsExactlyInAnyOrder(1L);

        group = groups.get(2);
        assertThat(group.getSteps()).isNotEmpty().hasSize(1);
        step = group.findByTargetId(1L).orElseThrow(NoSuchElementException::new);
        assertThat(step.getParentWorkItems()).isEmpty();
    }
}
