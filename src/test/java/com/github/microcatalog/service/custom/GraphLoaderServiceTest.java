package com.github.microcatalog.service.custom;

import com.github.microcatalog.MappersConfig;
import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import com.github.microcatalog.utils.DependencyBuilder;
import com.github.microcatalog.utils.MicroserviceBuilder;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.github.microcatalog.service.dto.custom.builder.MicroserviceDtoBuilder.aMicroserviceDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest(classes = {GraphLoaderService.class, MappersConfig.class})
class GraphLoaderServiceTest {

    @MockBean
    private MicroserviceRepository microserviceRepository;

    @MockBean
    private DependencyRepository dependencyRepository;

    @Autowired
    private GraphLoaderService sut;

    @Test
    void loadEmptyGraph() {
        given(microserviceRepository.findAll()).willReturn(Collections.emptyList());
        given(dependencyRepository.findAll()).willReturn(Collections.emptyList());

        Graph<MicroserviceDto, DefaultEdge> graph = sut.loadGraph();
        assertThat(graph).isNotNull();
        assertThat(graph.vertexSet()).isEmpty();
        assertThat(graph.edgeSet()).isEmpty();

        then(microserviceRepository).should().findAll();
        then(dependencyRepository).should().findAll();
    }

    @Test
    void loadSampleGraph() {
        final List<Microservice> microservicesData = createMicroservicesData();
        final List<MicroserviceDto> dtos = microservicesData.stream()
            .map(m -> aMicroserviceDto().withId(m.getId()).build()).collect(Collectors.toList());

        given(microserviceRepository.findAll()).willReturn(microservicesData);
        given(dependencyRepository.findAll()).willReturn(createDependenciesWithCycleInSameComponent());

        Graph<MicroserviceDto, DefaultEdge> graph = sut.loadGraph();
        assertThat(graph).isNotNull();
        assertThat(graph.vertexSet()).containsExactlyInAnyOrder(dtos.toArray(new MicroserviceDto[0]));

        Set<DefaultEdge> expectedEdges = new HashSet<>();
        expectedEdges.add(graph.getEdge(dtos.get(0), dtos.get(1)));
        expectedEdges.add(graph.getEdge(dtos.get(1), dtos.get(2)));
        expectedEdges.add(graph.getEdge(dtos.get(2), dtos.get(0)));
        expectedEdges.add(graph.getEdge(dtos.get(3), dtos.get(4)));
        expectedEdges.add(graph.getEdge(dtos.get(4), dtos.get(5)));

        assertThat(graph.edgeSet()).containsExactlyInAnyOrder(expectedEdges.toArray(new DefaultEdge[0]));

        then(microserviceRepository).should().findAll();
        then(dependencyRepository).should().findAll();
    }

    /**
     * Graph will contain 6 vertices: 1..6
     *
     * @return microservices
     */
    private List<Microservice> createMicroservicesData() {
        return LongStream.rangeClosed(0, 5)
            .mapToObj(i -> new MicroserviceBuilder().withId(i).build())
            .collect(Collectors.toList());
    }


    /**
     * Graph will contain two connected components, one of them has cycle
     * <p>
     * First component with cycle 0->1->2->0
     * Second component without cycle 3->4, 4->5
     *
     * @return dependencies
     */
    private List<Dependency> createDependenciesWithCycleInSameComponent() {
        final List<Dependency> dependencies = new ArrayList<>();

        // First component with cycle 0->1->2->0
        dependencies.add(new DependencyBuilder()
            .withId(1L).withSource(0L).withTarget(1L)
            .build());
        dependencies.add(new DependencyBuilder()
            .withId(2L).withSource(1L).withTarget(2L)
            .build());
        dependencies.add(new DependencyBuilder()
            .withId(3L).withSource(2L).withTarget(0L)
            .build());

        // Second component without cycle 3->4, 4->5
        dependencies.add(new DependencyBuilder()
            .withId(4L).withSource(3L).withTarget(4L)
            .build());
        dependencies.add(new DependencyBuilder()
            .withId(5L).withSource(4L).withTarget(5L)
            .build());


        return dependencies;
    }
}
