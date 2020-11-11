package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.DuplicateDependencyException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import com.github.microcatalog.service.mapper.DependencyMapper;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class DependencyService {

    private final Logger log = LoggerFactory.getLogger(DependencyService.class);

    private final GraphLoaderService graphLoaderService;
    private final DependencyRepository dependencyRepository;
    private final MicroserviceRepository microserviceRepository;
    private final DependencyMapper dependencyMapper;

    public DependencyService(GraphLoaderService graphLoaderService, DependencyRepository dependencyRepository,
                             MicroserviceRepository microserviceRepository, DependencyMapper dependencyMapper) {
        this.graphLoaderService = graphLoaderService;
        this.dependencyRepository = dependencyRepository;
        this.microserviceRepository = microserviceRepository;
        this.dependencyMapper = dependencyMapper;
    }

    public Dependency create(final Dependency dependency) {
        if (dependency.getId() != null) {
            throw new IllegalArgumentException("A new dependency can not already have an id");
        }

        final DependencyDto dto = dependencyDto(dependency);
        validateSelfCycle(dto);
        validateIfAdded(dto);

        return dependencyRepository.save(dependency);
    }

    /**
     * Creates dependency with given name
     *
     * @param dependencyName name of new dependency
     * @param sourceName     source of dependency
     * @param targetName     target of dependency
     * @return created dependency
     */
    public DependencyDto create(final String dependencyName, final String sourceName, final String targetName) {
        final Optional<Microservice> maybeSource = microserviceRepository.findByName(sourceName).stream().findFirst();
        final Optional<Microservice> maybeTarget = microserviceRepository.findByName(targetName).stream().findFirst();

        if (!maybeSource.isPresent()) {
            throw new IllegalArgumentException(String.format("Source microservice with name [%s] doesn't exist", sourceName));
        }

        if (!maybeTarget.isPresent()) {
            throw new IllegalArgumentException(String.format("Target microservice with name [%s] doesn't exist", targetName));
        }

        final Dependency dependency = new Dependency().name(dependencyName)
            .source(maybeSource.get())
            .target(maybeTarget.get());
        return dependencyDto(create(dependency));
    }

    public Dependency update(final Dependency dependency) {
        if (dependency.getId() == null) {
            throw new IllegalArgumentException("Updating non-persistent entity without id");
        }

        final DependencyDto dto = dependencyDto(dependency);
        validateSelfCycle(dto);
        validateIfUpdated(dto);

        return dependencyRepository.save(dependency);
    }

    public List<Dependency> findAll() {
        return dependencyRepository.findAll();
    }

    public Optional<Dependency> findById(Long id) {
        return dependencyRepository.findById(id);
    }

    public List<DependencyDto> findAllById(final List<Long> ids) {
        return dependencyRepository.findAllById(ids).stream()
            .map(dependencyMapper::dependencyToDto)
            .collect(Collectors.toList());
    }

    public Long deleteByName(final String name) {
        return dependencyRepository.deleteByName(name);
    }

    public void deleteById(Long id) {
        dependencyRepository.deleteById(id);
    }

    private void validateSelfCycle(final DependencyDto dependency) {
        if (dependency == null) {
            return;
        }

        if (dependency.getSource() == null || dependency.getTarget() == null) {
            return;
        }

        final MicroserviceDto source = dependency.getSource();
        if (Objects.equals(source.getId(), dependency.getTarget().getId())) {
            throw new SelfCircularException("Source of dependency can't be the same as target", source);
        }
    }

    private void validateIfAdded(final DependencyDto toBeAdded) {
        final Graph<MicroserviceDto, DefaultEdge> graph = graphLoaderService.loadGraph();

        checkDuplicateWillBeIntroduced(graph, toBeAdded);

        graph.addEdge(toBeAdded.getSource(), toBeAdded.getTarget());

        checkCycles(graph);
    }

    private void validateIfUpdated(final DependencyDto dependency) {
        final Graph<MicroserviceDto, DefaultEdge> graph = graphLoaderService.loadGraph();
        final Dependency persistent = dependencyRepository.findById(dependency.getId())
            .orElseThrow(() -> new IllegalArgumentException("Trying to update dependency, but it was removed from data source"));
        final DependencyDto persistentDto = dependencyDto(persistent);

        // check what will be if we remove existing edge and replace it with updated one

        final DefaultEdge currentEdge = graph.getEdge(persistentDto.getSource(), persistentDto.getTarget());
        graph.removeEdge(currentEdge);

        checkDuplicateWillBeIntroduced(graph, dependency);

        graph.addEdge(dependency.getSource(), dependency.getTarget());

        checkCycles(graph);
    }

    private void checkDuplicateWillBeIntroduced(final Graph<MicroserviceDto, DefaultEdge> graph, final DependencyDto dependency) {
        final DefaultEdge existingEdge = graph.getEdge(dependency.getSource(), dependency.getTarget());
        if (existingEdge != null) {
            throw new DuplicateDependencyException("Dependency already exists", dependency);
        }
    }

    private void checkCycles(final Graph<MicroserviceDto, DefaultEdge> graph) {
        final CycleDetector<MicroserviceDto, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
        if (cycleDetector.detectCycles()) {
            final Set<MicroserviceDto> cycles = cycleDetector.findCycles();
            log.debug("Cycles: {}", cycles);

            throw new CircularDependenciesException("Circular dependency will be introduced", cycles);
        }
    }

    private DependencyDto dependencyDto(final Dependency dataEntity) {
        return dependencyMapper.dependencyToDto(dataEntity);
    }
}
