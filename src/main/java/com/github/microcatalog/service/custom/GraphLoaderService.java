package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import com.github.microcatalog.service.mapper.MicroserviceMapper;
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
    private final MicroserviceMapper mapper;

    public GraphLoaderService(MicroserviceRepository microserviceRepository, DependencyRepository dependencyRepository, MicroserviceMapper mapper) {
        this.microserviceRepository = microserviceRepository;
        this.dependencyRepository = dependencyRepository;
        this.mapper = mapper;
    }

    public Graph<MicroserviceDto, DefaultEdge> loadGraph() {
        List<Microservice> microservices = microserviceRepository.findAll();
        List<Dependency> dependencies = dependencyRepository.findAll();

        return createGraph(microservices, dependencies);
    }

    private Graph<MicroserviceDto, DefaultEdge> createGraph(final List<Microservice> nodes, final List<Dependency> edges) {
        final Graph<MicroserviceDto, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        nodes.forEach(n -> graph.addVertex(dto(n)));
        edges.forEach(d -> graph.addEdge(dto(d.getSource()), dto(d.getTarget())));

        return graph;
    }

    private MicroserviceDto dto(final Microservice microservice) {
        return mapper.microserviceToDto(microservice);
    }
}
