package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.service.custom.exceptions.MicroserviceNotFoundException;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class GraphOperationsService {

    private final Logger log = LoggerFactory.getLogger(GraphOperationsService.class);

    protected final GraphLoaderService graphLoaderService;

    protected GraphOperationsService(GraphLoaderService graphLoaderService) {
        this.graphLoaderService = graphLoaderService;
    }

    protected GraphContext getConnectedSubgraphWithoutCycles(final long microserviceId) {
        final Graph<Microservice, DefaultEdge> graph = graphLoaderService.loadGraph();

        if (graph.vertexSet().isEmpty()) {
            return new GraphContext(graph, null);
        }

        final Optional<Microservice> maybeTarget = graph.vertexSet()
            .stream().filter(v -> Objects.equals(v.getId(), microserviceId)).findFirst();

        // can't build release path, cause microservice with given id is not present in graph
        if (!maybeTarget.isPresent()) {
            throw new MicroserviceNotFoundException("Microservice not found", microserviceId);
        }

        final Microservice target = maybeTarget.get();

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

        return new GraphContext(targetSubgraph, target);
    }

    protected static class GraphContext {
        private final Graph<Microservice, DefaultEdge> graph;
        private final Microservice target;

        public GraphContext(Graph<Microservice, DefaultEdge> graph, Microservice target) {
            this.graph = graph;
            this.target = target;
        }

        public boolean hasEmptyGraph() {
            if (this.graph == null) {
                return true;
            }

            return graph.vertexSet().isEmpty();
        }

        public Graph<Microservice, DefaultEdge> getGraph() {
            return graph;
        }

        public Microservice getTarget() {
            return target;
        }
    }
}
