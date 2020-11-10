package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.custom.impact.analysis.Group;
import com.github.microcatalog.domain.custom.impact.analysis.Item;
import com.github.microcatalog.domain.custom.impact.analysis.Result;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImpactAnalysisService extends GraphOperationsService {

    private final Logger log = LoggerFactory.getLogger(ImpactAnalysisService.class);

    public ImpactAnalysisService(GraphLoaderService graphLoaderService) {
        super(graphLoaderService);
    }

    public Optional<Result> calculate(final Long microserviceId) {
        final GraphContext context = getConnectedSubgraphWithoutCycles(microserviceId);
        if (context.hasEmptyGraph()) {
            return Optional.empty();
        }

        final Graph<Microservice, DefaultEdge> reversed = new EdgeReversedGraph<>(context.getGraph());

        // Calculate all vertices, you can reach from target
        final Set<Microservice> affectedMicroservices = new HashSet<>();
        GraphIterator<Microservice, DefaultEdge> iterator = new DepthFirstIterator<>(reversed, context.getTarget());
        while (iterator.hasNext()) {
            affectedMicroservices.add(iterator.next());
        }

        final Graph<Microservice, DefaultEdge> affectedGraph = new AsSubgraph<>(reversed, affectedMicroservices);

        final Result result = new Result().createdOn(Instant.now()).target(context.getTarget());

        do {
            final List<Microservice> verticesWithoutIncomingEdges = affectedGraph.vertexSet().stream()
                .filter(v -> affectedGraph.incomingEdgesOf(v).isEmpty())
                .collect(Collectors.toList());
            log.debug("Leaves: {}", verticesWithoutIncomingEdges);

            final Group group = createGroup(affectedGraph, verticesWithoutIncomingEdges);
            result.addGroup(group);

            verticesWithoutIncomingEdges.forEach(affectedGraph::removeVertex);
        } while (!affectedGraph.vertexSet().isEmpty());

        return Optional.of(result);
    }

    private Group createGroup(final Graph<Microservice, DefaultEdge> graph, final List<Microservice> verticesWithoutIncomingEdges) {
        final Group group = new Group();

        verticesWithoutIncomingEdges.forEach(v -> {
            final Set<DefaultEdge> outgoingEdgesOf = graph.outgoingEdgesOf(v);
            final List<Microservice> siblings = new ArrayList<>();
            outgoingEdgesOf.forEach(e -> {
                final Microservice sibling = graph.getEdgeTarget(e);
                siblings.add(sibling);
            });

            group.addItem(new Item().target(v).siblings(siblings));
        });

        group.sortItemsById();


        return group;
    }

}
