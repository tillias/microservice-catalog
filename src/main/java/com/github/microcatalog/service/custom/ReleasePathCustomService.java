package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.custom.ReleaseGroup;
import com.github.microcatalog.domain.custom.ReleasePath;
import com.github.microcatalog.domain.custom.ReleaseStep;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.StringWriter;
import java.io.Writer;
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

    private final GraphLoaderService graphLoaderService;

    public ReleasePathCustomService(GraphLoaderService graphLoaderService) {
        this.graphLoaderService = graphLoaderService;
    }


    public Optional<ReleasePath> getReleasePath(final Long microserviceId) {
        final Graph<Microservice, DefaultEdge> graph = graphLoaderService.loadGraph();
        final Microservice target = new Microservice();
        target.setId(microserviceId);

        // can't build release path, cause microservice with given id is not present in graph
        if (!graph.containsVertex(target)) {
            return Optional.empty();
        }

        final ConnectivityInspector<Microservice, DefaultEdge> inspector = new ConnectivityInspector<>(graph);
        final Set<Microservice> connectedSet = inspector.connectedSetOf(target);

        // Connected subgraph, that contains target microservice
        final AsSubgraph<Microservice, DefaultEdge> targetSubgraph = new AsSubgraph<>(graph, connectedSet);
        log.debug("Connected subgraph, that contains target microservice: {}", targetSubgraph);

        final CycleDetector<Microservice, DefaultEdge> cycleDetector = new CycleDetector<>(targetSubgraph);
        if (cycleDetector.detectCycles()) {
            final Set<Microservice> cycles = cycleDetector.findCycles();
            throw new IllegalArgumentException(String.format("There are cyclic dependencies between microservices : %s", cycles));
        }

        final Set<Microservice> pathMicroservices = new HashSet<>();
        GraphIterator<Microservice, DefaultEdge> iterator = new DepthFirstIterator<>(targetSubgraph, target);
        while (iterator.hasNext()) {
            pathMicroservices.add(iterator.next());
        }

        // TODO new use-case and visualisation
        // For each element of pathSet calculate all nodes who depends on items from pathSet. Microservices which will be possibly affected if we build target and it's direct dependencies

        final Graph<Microservice, DefaultEdge> pathGraph = new AsSubgraph<>(targetSubgraph, pathMicroservices);
        log.debug("Connected subgraph, which contains all paths from target microservice to it's dependencies {}", pathGraph);

        final Graph<Microservice, DefaultEdge> reversed = new EdgeReversedGraph<>(pathGraph);
        return Optional.of(convert(reversed, target));
    }

    private ReleasePath convert(final Graph<Microservice, DefaultEdge> graph, final Microservice target) {
        final ReleasePath result = new ReleasePath();
        result.setCreatedOn(Instant.now());
        result.setTarget(target);

        final List<ReleaseGroup> groups = new ArrayList<>();

        do {
            final List<Microservice> verticesWithoutIncomingEdges = graph.vertexSet().stream()
                .filter(v -> graph.incomingEdgesOf(v).isEmpty())
                .collect(Collectors.toList());
            log.debug("Leaves: {}", verticesWithoutIncomingEdges);

            final ReleaseGroup group = new ReleaseGroup();
            group.setSteps(convertSteps(verticesWithoutIncomingEdges, graph));
            groups.add(group);

            verticesWithoutIncomingEdges.forEach(graph::removeVertex);
        } while (!graph.vertexSet().isEmpty());

        result.setGroups(groups);

        return result;
    }

    private Set<ReleaseStep> convertSteps(final List<Microservice> verticesWithoutIncomingEdges,
                                          final Graph<Microservice, DefaultEdge> graph) {
        final Set<ReleaseStep> result = new HashSet<>();

        verticesWithoutIncomingEdges.forEach(microservice -> {
            final List<Microservice> parentWorkItems = new ArrayList<>();

            final Set<DefaultEdge> outgoingEdges = graph.outgoingEdgesOf(microservice);
            for (DefaultEdge e : outgoingEdges) {
                final Microservice edgeTarget = graph.getEdgeTarget(e);
                parentWorkItems.add(edgeTarget);
            }

            result.add(
                new ReleaseStep()
                    .workItem(microservice)
                    .parentWorkItems(parentWorkItems)
            );
        });

        return result;
    }
}
