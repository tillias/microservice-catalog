package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.*;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.repository.ReleasePathRepository;
import com.google.common.graph.Graphs;
import org.apache.commons.lang3.NotImplementedException;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for release path calculation
 */
@Service
@Transactional
public class ReleasePathCustomService {

    private final Logger log = LoggerFactory.getLogger(ReleasePathCustomService.class);

    private final ReleasePathRepository releasePathRepository;
    private final MicroserviceRepository microserviceRepository;
    private final DependencyRepository dependencyRepository;

    public ReleasePathCustomService(ReleasePathRepository releasePathRepository,
                                    MicroserviceRepository microserviceRepository,
                                    DependencyRepository dependencyRepository) {
        this.releasePathRepository = releasePathRepository;
        this.microserviceRepository = microserviceRepository;
        this.dependencyRepository = dependencyRepository;
    }

    public Optional<ReleasePath> getReleasePath(final Long microserviceId) {
        final List<Microservice> microservices = microserviceRepository.findAll();
        final List<Dependency> dependencies = dependencyRepository.findAll();
        final Optional<Microservice> maybeTarget =
            microservices.stream().filter(m -> Objects.equals(m.getId(), microserviceId)).findFirst();

        if (!maybeTarget.isPresent()) {
            return Optional.empty();
        }

        final Graph<Long, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        microservices.forEach(m -> graph.addVertex(m.getId()));
        dependencies.forEach(d -> graph.addEdge(d.getSource().getId(), d.getTarget().getId()));

        final ConnectivityInspector<Long, DefaultEdge> inspector = new ConnectivityInspector<>(graph);
        final Microservice target = maybeTarget.get();
        final Set<Long> connectedSet = inspector.connectedSetOf(target.getId());

        // Connected subgraph, that contains target microservice
        final AsSubgraph<Long, DefaultEdge> targetSubgraph = new AsSubgraph<>(graph, connectedSet);
        log.debug(targetSubgraph.toString());

        final CycleDetector<Long, DefaultEdge> cycleDetector = new CycleDetector<>(targetSubgraph);
        if (cycleDetector.detectCycles()) {
            final Set<Long> cycles = cycleDetector.findCycles();
            throw new IllegalArgumentException(String.format("There are cyclic dependencies between microservices : %s", cycles));
        }

        final Set<Long> pathMicroservices = new HashSet<>();
        GraphIterator<Long, DefaultEdge> iterator = new DepthFirstIterator<>(targetSubgraph, target.getId());
        while (iterator.hasNext()) {
            pathMicroservices.add(iterator.next());
        }

        // TODO new use-case and visualisation
        // For each element of pathSet calculate all nodes who depends on items from pathSet. Microservices which will be possibly affected if we build target and it's direct dependencies

        // Connected subgraph, which contains all paths from target to it's dependencies.
        final Graph<Long, DefaultEdge> pathGraph = new AsSubgraph<>(targetSubgraph, pathMicroservices);
        log.debug(pathGraph.toString());

        final Graph<Long, DefaultEdge> reversed = new EdgeReversedGraph<>(pathGraph);

        return Optional.of(convert(reversed, microservices, target));
    }

    private ReleasePath convert(final Graph<Long, DefaultEdge> graph, final List<Microservice> microservices, final Microservice target) {
        final ReleasePath result = new ReleasePath();
        result.setCreatedOn(Instant.now());
        result.setTarget(target);

        final Set<ReleaseGroup> groups = new HashSet<>();
        final Map<Long, Microservice> microserviceMap = microservices.stream()
            .collect(Collectors.toMap(Microservice::getId, m -> m));

        int groupIndex = 0;
        do {
            final List<Long> verticesWithoutIncomingEdges = graph.vertexSet().stream()
                .filter(v -> graph.incomingEdgesOf(v).isEmpty())
                .collect(Collectors.toList());
            log.debug("Leaves: {}", verticesWithoutIncomingEdges);

            final ReleaseGroup group = new ReleaseGroup();
            group.setSteps(convertSteps(microserviceMap, verticesWithoutIncomingEdges));
            group.setOrder(groupIndex++);
            groups.add(group);

            verticesWithoutIncomingEdges.forEach(graph::removeVertex);
        } while (!graph.vertexSet().isEmpty());

        result.setGroups(groups);

        return result;
    }

    private Set<ReleaseStep> convertSteps(final Map<Long, Microservice> microserviceMap, List<Long> microserviceIds) {
        final Set<ReleaseStep> result = new HashSet<>();

        microserviceIds.forEach(id -> {
            final ReleaseStep step = new ReleaseStep();
            step.setWorkItem(microserviceMap.get(id));
            result.add(step);
        });

        return result;
    }
}
