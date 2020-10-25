package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.*;
import com.github.microcatalog.domain.custom.ReleaseGroup;
import com.github.microcatalog.domain.custom.ReleasePath;
import com.github.microcatalog.domain.custom.ReleaseStep;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.utils.DependencyBuilder;
import com.github.microcatalog.utils.MicroserviceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = ReleasePathCustomService.class)
public class ReleasePathCustomServiceTest {

    @MockBean
    private MicroserviceRepository microserviceRepository;

    @MockBean
    private DependencyRepository dependencyRepository;

    @Autowired
    private ReleasePathCustomService service;

    @Test
    public void getReleasePath_NoCycles_Success() {
        given(microserviceRepository.findAll()).willReturn(createMicroservices());

        given(dependencyRepository.findAll()).willReturn(createDependencies());

        Optional<ReleasePath> maybePath = service.getReleasePath(1L);
        assertThat(maybePath).isPresent();

        ReleasePath path = maybePath.get();

        Set<ReleaseStep> steps = getStep(path, 0);
        assertThat(steps).isNotEmpty()
            .extracting(ReleaseStep::getWorkItem).extracting(Microservice::getId).containsExactlyInAnyOrder(5L, 7L);

        steps = getStep(path, 1);
        assertThat(steps).isNotEmpty()
            .extracting(ReleaseStep::getWorkItem).extracting(Microservice::getId).containsExactly(4L);

        steps = getStep(path, 2);
        assertThat(steps).isNotEmpty()
            .extracting(ReleaseStep::getWorkItem).extracting(Microservice::getId).containsExactly(2L);

        steps = getStep(path, 3);
        assertThat(steps).isNotEmpty()
            .extracting(ReleaseStep::getWorkItem).extracting(Microservice::getId).containsExactly(1L);
    }

    @Test
    public void getReleasePath_ContainsCyclesInSameComponent_ExceptionIsThrown() {
        fail("Not yet implemented");
    }

    @Test
    public void getReleasePath_ContainsCyclesInOtherComponent_Success() {
        fail("Not yet implemented");
    }

    private Set<ReleaseStep> getStep(ReleasePath path, int groupIndex) {
        ReleaseGroup first = path.getGroups().get(groupIndex);
        if (first != null) {
            return first.getSteps();
        } else {
            return Collections.emptySet();
        }
    }

    private List<Microservice> createMicroservices() {
        return LongStream.rangeClosed(1, 12)
            .mapToObj(i -> new MicroserviceBuilder().withId(i).build())
            .collect(Collectors.toList());
    }

    private List<Dependency> createDependencies() {
        final List<Dependency> dependencies = new ArrayList<>();

        dependencies.add(new DependencyBuilder()
            .withId(1L).withSource(1L).withTarget(2L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(2L).withSource(2L).withTarget(4L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(3L).withSource(6L).withTarget(4L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(4L).withSource(4L).withTarget(5L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(5L).withSource(4L).withTarget(7L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(6L).withSource(8L).withTarget(9L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(7L).withSource(3L).withTarget(10L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(8L).withSource(3L).withTarget(11L)
            .build());

        dependencies.add(new DependencyBuilder()
            .withId(9L).withSource(12L).withTarget(1L)
            .build());

        return dependencies;
    }
}
