package com.github.microcatalog.service;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.utils.MicroserviceBuilder;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.StringReader;

/**
 * Test utils for graph manipulation
 */
public class GraphUtils {
    public static Graph<Microservice, DefaultEdge> createGraph(final String dot) {
        final DOTImporter<Microservice, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(s -> new MicroserviceBuilder().withId(Long.valueOf(s)).build());

        final Graph<Microservice, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        importer.importGraph(graph, new StringReader(dot));

        return graph;
    }
}
