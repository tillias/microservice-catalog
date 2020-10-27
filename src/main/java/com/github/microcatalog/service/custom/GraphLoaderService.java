package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GraphLoaderService {

    private final MicroserviceRepository microserviceRepository;
    private final DependencyRepository dependencyRepository;

    public GraphLoaderService(MicroserviceRepository microserviceRepository, DependencyRepository dependencyRepository) {
        this.microserviceRepository = microserviceRepository;
        this.dependencyRepository = dependencyRepository;
    }

    public Graph<Microservice, DefaultEdge> loadGraph() {
        List<Microservice> microservices = microserviceRepository.findAll();
        List<Dependency> dependencies = dependencyRepository.findAll();

        return createGraph(microservices, dependencies);
    }

    private Graph<Microservice, DefaultEdge> createGraph(final List<Microservice> nodes, final List<Dependency> edges) {
        final Graph<Microservice, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        nodes.forEach(graph::addVertex);
        edges.forEach(d -> graph.addEdge(d.getSource(), d.getTarget()));

        return graph;
    }
}
