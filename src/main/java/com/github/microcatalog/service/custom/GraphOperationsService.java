package com.github.microcatalog.service.custom;

import com.github.microcatalog.service.custom.exceptions.MicroserviceNotFoundException;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
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
        final Graph<MicroserviceDto, DefaultEdge> graph = graphLoaderService.loadGraph();

        if (graph.vertexSet().isEmpty()) {
            return new GraphContext(graph, null);
        }

        final Optional<MicroserviceDto> maybeTarget = graph.vertexSet()
            .stream().filter(v -> Objects.equals(v.getId(), microserviceId)).findFirst();

        // can't build release path, cause microservice with given id is not present in graph
        if (!maybeTarget.isPresent()) {
            throw new MicroserviceNotFoundException("Microservice not found", microserviceId);
        }

        final MicroserviceDto target = maybeTarget.get();

        final ConnectivityInspector<MicroserviceDto, DefaultEdge> inspector = new ConnectivityInspector<>(graph);
        final Set<MicroserviceDto> connectedSet = inspector.connectedSetOf(target);

        // Connected subgraph, that contains target microservice
        final AsSubgraph<MicroserviceDto, DefaultEdge> targetSubgraph = new AsSubgraph<>(graph, connectedSet);
        log.debug("Connected subgraph, that contains target microservice: {}", targetSubgraph);

        final CycleDetector<MicroserviceDto, DefaultEdge> cycleDetector = new CycleDetector<>(targetSubgraph);
        if (cycleDetector.detectCycles()) {
            final Set<MicroserviceDto> cycles = cycleDetector.findCycles();
            throw new IllegalArgumentException(String.format("There are cyclic dependencies between microservices : %s", cycles));
        }

        return new GraphContext(targetSubgraph, target);
    }

    protected static class GraphContext {
        private final Graph<MicroserviceDto, DefaultEdge> graph;
        private final MicroserviceDto target;

        public GraphContext(Graph<MicroserviceDto, DefaultEdge> graph, MicroserviceDto target) {
            this.graph = graph;
            this.target = target;
        }

        public boolean hasEmptyGraph() {
            if (this.graph == null) {
                return true;
            }

            return graph.vertexSet().isEmpty();
        }

        public Graph<MicroserviceDto, DefaultEdge> getGraph() {
            return graph;
        }

        public MicroserviceDto getTarget() {
            return target;
        }
    }
}
