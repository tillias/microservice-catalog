package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.custom.impact.analysis.Group;
import com.github.microcatalog.domain.custom.impact.analysis.Item;
import com.github.microcatalog.domain.custom.impact.analysis.Result;
import com.github.microcatalog.service.GraphUtils;
import com.github.microcatalog.service.custom.exceptions.MicroserviceNotFoundException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {ImpactAnalysisService.class})
class ImpactAnalysisServiceTest {

    @MockBean
    private GraphLoaderService graphLoaderService;

    @Autowired
    private ImpactAnalysisService sut;

    @Test
    void calculate_EmptyGraph_EmptyResult() {
        given(graphLoaderService.loadGraph())
            .willReturn(new DefaultDirectedGraph<>(DefaultEdge.class));

        Optional<Result> result = sut.calculate(4L);
        assertThat(result).isNotNull().isNotPresent();
    }

    @Test
    void calculate_NodeOutsideGraph_EmptyResult() {
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

        assertThatThrownBy(() -> sut.calculate(4L))
            .isInstanceOf(MicroserviceNotFoundException.class)
            .hasMessageStartingWith("Microservice not found")
            .asInstanceOf(InstanceOfAssertFactories.type(MicroserviceNotFoundException.class))
            .extracting(MicroserviceNotFoundException::getMicroserviceId)
            .isEqualTo(4L);
    }

    @Test
    void calculate_NoCycles_Success() {
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

        final Optional<Result> maybeResult = sut.calculate(5L);
        assertThat(maybeResult).isNotNull().isPresent();

        final Result result = maybeResult.get();
        assertThat(result.getGroups()).isNotEmpty().hasSize(6);

        final List<Group> groups = result.getGroups();

        Group group = groups.get(0);
        assertThat(group.getItems()).isNotEmpty().hasSize(1);
        Item item = group.findByTargetId(5L).orElseThrow();
        assertThatItemHasSiblings(item, 4L, 7L);

        group = groups.get(1);
        assertThat(group.getItems()).isNotEmpty().hasSize(1);
        item = group.findByTargetId(7L).orElseThrow();
        assertThatItemHasSiblings(item, 4L);

        group = groups.get(2);
        assertThat(group.getItems()).isNotEmpty().hasSize(1);
        item = group.findByTargetId(4L).orElseThrow();
        assertThatItemHasSiblings(item, 2L, 6L);

        group = groups.get(3);
        assertThat(group.getItems()).isNotEmpty().hasSize(2);
        item = group.findByTargetId(6L).orElseThrow();
        assertThatItemHasNoSiblings(item);
        item = group.findByTargetId(2L).orElseThrow();
        assertThatItemHasSiblings(item, 1L);

        group = groups.get(4);
        assertThat(group.getItems()).isNotEmpty().hasSize(1);
        item = group.findByTargetId(1L).orElseThrow();
        assertThatItemHasSiblings(item, 10L, 12L);

        group = groups.get(5);
        assertThat(group.getItems()).isNotEmpty().hasSize(2);
        item = group.findByTargetId(10L).orElseThrow();
        assertThatItemHasNoSiblings(item);
        item = group.findByTargetId(12L).orElseThrow();
        assertThatItemHasNoSiblings(item);
    }

    @Test
    void calculate_ContainsCyclesInSameComponent_ExceptionIsThrown() {
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
                sut.calculate(1L)
            )
            .withMessageStartingWith("There are cyclic dependencies between microservices");
    }


    @Test
    void calculate_ContainsCyclesInOtherComponent_Success() {
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

        Optional<Result> maybeResult = sut.calculate(4L);
        assertThat(maybeResult).isPresent();

        Result result = maybeResult.get();
        assertThat(result.getGroups()).isNotEmpty().hasSize(3);

        final List<Group> groups = result.getGroups();

        Group group = groups.get(0);
        assertThat(group.getItems()).isNotEmpty().hasSize(1);
        Item item = group.findByTargetId(4L).orElseThrow();
        assertThatItemHasSiblings(item, 2L);

        group = groups.get(1);
        assertThat(group.getItems()).isNotEmpty().hasSize(1);
        item = group.findByTargetId(2L).orElseThrow();
        assertThatItemHasSiblings(item, 1L);

        group = groups.get(2);
        assertThat(group.getItems()).isNotEmpty().hasSize(1);
        item = group.findByTargetId(1L).orElseThrow();
        assertThatItemHasNoSiblings(item);
    }

    private void assertThatItemHasNoSiblings(Item item) {
        assertThat(item.getSiblings()).isEmpty();
    }

    private void assertThatItemHasSiblings(Item item, Long... siblingsIds) {
        assertThat(item.getSiblings())
            .extracting(Microservice::getId)
            .containsExactlyInAnyOrder(siblingsIds);
    }
}
