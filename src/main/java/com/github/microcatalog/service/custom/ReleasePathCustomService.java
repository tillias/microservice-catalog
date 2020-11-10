package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.custom.ReleaseGroup;
import com.github.microcatalog.domain.custom.ReleasePath;
import com.github.microcatalog.domain.custom.ReleaseStep;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for release path calculation
 */
@Service
public class ReleasePathCustomService extends GraphOperationsService {

    private final Logger log = LoggerFactory.getLogger(ReleasePathCustomService.class);

    public ReleasePathCustomService(GraphLoaderService graphLoaderService) {
        super(graphLoaderService);
    }

    public Optional<ReleasePath> getReleasePath(final Long microserviceId) {
        final GraphContext context = getConnectedSubgraphWithoutCycles(microserviceId);
        if (context.hasEmptyGraph()) {
            return Optional.empty();
        }

        final Set<Microservice> pathMicroservices = new HashSet<>();
        GraphIterator<Microservice, DefaultEdge> iterator = new DepthFirstIterator<>(context.getGraph(), context.getTarget());
        while (iterator.hasNext()) {
            pathMicroservices.add(iterator.next());
        }

        final Graph<Microservice, DefaultEdge> pathGraph = new AsSubgraph<>(context.getGraph(), pathMicroservices);
        log.debug("Connected subgraph, which contains all paths from target microservice to it's dependencies {}", pathGraph);

        final Graph<Microservice, DefaultEdge> reversed = new EdgeReversedGraph<>(pathGraph);
        return Optional.of(convert(reversed, context.getTarget()));
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
            group.sortStepsById();
            groups.add(group);

            verticesWithoutIncomingEdges.forEach(graph::removeVertex);
        } while (!graph.vertexSet().isEmpty());

        result.setGroups(groups);

        return result;
    }

    private List<ReleaseStep> convertSteps(final List<Microservice> verticesWithoutIncomingEdges,
                                           final Graph<Microservice, DefaultEdge> graph) {
        final List<ReleaseStep> result = new ArrayList<>();

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
