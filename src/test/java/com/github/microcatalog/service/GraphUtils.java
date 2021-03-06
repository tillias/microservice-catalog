package com.github.microcatalog.service;

import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.StringReader;

import static com.github.microcatalog.service.dto.custom.builder.MicroserviceDtoBuilder.aMicroserviceDto;

/**
 * Test utils for graph manipulation
 */
public class GraphUtils {
    public static Graph<MicroserviceDto, DefaultEdge> createGraph(final String dot) {
        final DOTImporter<MicroserviceDto, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(s -> aMicroserviceDto().withId(Long.valueOf(s)).build());

        final Graph<MicroserviceDto, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        importer.importGraph(graph, new StringReader(dot));

        return graph;
    }
}
