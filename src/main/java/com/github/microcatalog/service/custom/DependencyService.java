package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DependencyService {

    private final Logger log = LoggerFactory.getLogger(DependencyService.class);

    private final GraphLoaderService graphLoaderService;
    private final DependencyRepository repository;

    public DependencyService(GraphLoaderService graphLoaderService, DependencyRepository repository) {
        this.graphLoaderService = graphLoaderService;
        this.repository = repository;
    }

    public Dependency create(final Dependency dependency) {
        if (dependency.getId() != null) {
            throw new IllegalArgumentException("A new dependency can not already have an id");
        }

        validateSelfCycle(dependency);
        validateIfAdded(dependency);

        return repository.save(dependency);
    }

    public Dependency update(final Dependency dependency) {
        if (dependency.getId() == null) {
            throw new IllegalArgumentException("Updating non-persistent entity without id");
        }

        validateSelfCycle(dependency);
        validateIfUpdated(dependency);

        return repository.save(dependency);
    }

    public List<Dependency> findAll() {
        return repository.findAll();
    }

    public Optional<Dependency> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateSelfCycle(final Dependency dependency) {
        if (dependency == null) {
            return;
        }

        if (dependency.getSource() == null || dependency.getTarget() == null) {
            return;
        }

        final Microservice source = dependency.getSource();
        if (Objects.equals(source.getId(), dependency.getTarget().getId())) {
            throw new SelfCircularException("Source of dependency can't be the same as target", source);
        }
    }

    private void validateIfAdded(final Dependency toBeAdded) {
        final Graph<Microservice, DefaultEdge> graph = graphLoaderService.loadGraph();
        graph.addEdge(toBeAdded.getSource(), toBeAdded.getTarget());

        checkCycles(graph);
    }

    private void validateIfUpdated(final Dependency dependency) {
        final Graph<Microservice, DefaultEdge> graph = graphLoaderService.loadGraph();
        final Dependency persistent = repository.findById(dependency.getId())
            .orElseThrow(() -> new IllegalArgumentException("Trying to update dependency, but it was removed from data source"));

        // check what will be if we remove existing edge and replace it with updated one

        final DefaultEdge currentEdge = graph.getEdge(persistent.getSource(), persistent.getTarget());
        graph.removeEdge(currentEdge);

        graph.addEdge(dependency.getSource(), dependency.getTarget());

        checkCycles(graph);
    }

    private void checkCycles(final Graph<Microservice, DefaultEdge> graph) {
        final CycleDetector<Microservice, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
        if (cycleDetector.detectCycles()) {
            final Set<Microservice> cycles = cycleDetector.findCycles();
            log.debug("Cycles: {}", cycles);

            throw new CircularDependenciesException("Circular dependency will be introduced", cycles);
        }
    }
}
