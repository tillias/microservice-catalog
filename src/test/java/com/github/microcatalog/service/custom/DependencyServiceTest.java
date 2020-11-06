package com.github.microcatalog.service.custom;

import com.github.microcatalog.MappersConfig;
import com.github.microcatalog.TestUtils;
import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.service.GraphUtils;
import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.DuplicateDependencyException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import com.github.microcatalog.utils.MicroserviceBuilder;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.microcatalog.TestUtils.dependency;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest(classes = {DependencyService.class, MappersConfig.class})
class DependencyServiceTest {

    @MockBean
    private DependencyRepository repository;

    @MockBean
    private GraphLoaderService graphLoaderService;

    @Autowired
    private DependencyService service;

    @Test
    void findAll() {
        given(repository.findAll()).willReturn(Collections.emptyList());

        service.findAll();

        then(repository).should().findAll();
    }

    @Test
    void findById() {
        final Long id = 3L;

        given(repository.findById(id)).willReturn(Optional.of(dependency(id.intValue(), null, null)));

        Optional<Dependency> maybeDependency = service.findById(id);
        assertThat(maybeDependency).isPresent();
        assertThat(maybeDependency.get()).extracting(Dependency::getId).isEqualTo(id);

        then(repository).should().findById(id);
    }

    @Test
    void findAllById_EmptyList_NoException() {
        given(repository.findAllById(any())).willReturn(Collections.emptyList());

        final List<DependencyDto> dtos = service.findAllById(Arrays.asList(1L, 2L, 3L));
        assertThat(dtos).isEmpty();
    }

    @Test
    void findAllById() {
        given(repository.findAllById(Arrays.asList(1L, 2L, 3L))).willReturn(
            Arrays.asList(
                dependency(1, 1, 2),
                dependency(2, 2, 3),
                dependency(3, 3, 4)
            )
        );
        final List<DependencyDto> dtos = service.findAllById(Arrays.asList(1L, 2L, 3L));
        assertThat(dtos).hasSize(3).extracting(DependencyDto::getId, DependencyDto::getName)
            .containsExactlyInAnyOrder(
                tuple(1L, "1->2"),
                tuple(2L, "2->3"),
                tuple(3L, "3->4"));


        assertThat(TestUtils.getSource(dtos, 1L))
            .extracting(MicroserviceDto::getId, MicroserviceDto::getName)
            .containsExactly(1L, "1");
        assertThat(TestUtils.getTarget(dtos, 1L))
            .extracting(MicroserviceDto::getId, MicroserviceDto::getName)
            .containsExactly(2L, "2");

        assertThat(TestUtils.getSource(dtos, 2L))
            .extracting(MicroserviceDto::getId, MicroserviceDto::getName)
            .containsExactly(2L, "2");
        assertThat(TestUtils.getTarget(dtos, 2L))
            .extracting(MicroserviceDto::getId, MicroserviceDto::getName)
            .containsExactly(3L, "3");

        assertThat(TestUtils.getSource(dtos, 3L))
            .extracting(MicroserviceDto::getId, MicroserviceDto::getName)
            .containsExactly(3L, "3");
        assertThat(TestUtils.getTarget(dtos, 3L))
            .extracting(MicroserviceDto::getId, MicroserviceDto::getName)
            .containsExactly(4L, "4");
    }

    @Test
    void deleteById() {
        final long id = 3L;

        service.deleteById(id);

        then(repository).should().deleteById(id);
    }

    @Test
    void create_WithId_ExceptionIsThrown() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> service.create(dependency(1, null, null)))
            .withMessageStartingWith("A new dependency can not already have an id");
    }

    @Test
    void create_SelfCycle_ExceptionIsThrown() {
        final Dependency dependency = dependency(null, 2, 2);

        assertThatThrownBy(() -> service.create(dependency))
            .isInstanceOf(SelfCircularException.class)
            .hasMessageStartingWith("Source of dependency can't be the same as target");
    }

    @Test
    void create_WillIntroduceCircularDependencies_ExceptionIsThrown() {
        given(graphLoaderService.loadGraph()).willReturn(
            GraphUtils.createGraph(
                String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3; 4;",
                    "1 -> 2;",
                    "2 -> 3;",
                    "3 -> 4;}"
                )
            )
        );

        final Dependency dependency = dependency(null, 4, 1);

        assertThatThrownBy(() -> service.create(dependency))
            .isInstanceOf(CircularDependenciesException.class)
            .hasMessageStartingWith("Circular dependency will be introduced")
            .extracting("cycles", InstanceOfAssertFactories.ITERABLE)
            .extracting("id")
            .containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    void create_DuplicateWillBeIntroduced_ExceptionIsThrown() {
        given(graphLoaderService.loadGraph()).willReturn(
            GraphUtils.createGraph(
                String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3;",
                    "1 -> 2;",
                    "2 -> 3;}"
                )
            )
        );

        final Dependency dependency = dependency(null, 1, 2);

        assertThatThrownBy(() -> service.create(dependency))
            .isInstanceOf(DuplicateDependencyException.class)
            .hasMessageStartingWith("Dependency already exists")
            .extracting("dependency", InstanceOfAssertFactories.type(Dependency.class))
            .extracting(Dependency::getSource, Dependency::getTarget)
            .containsExactlyInAnyOrder(
                new MicroserviceBuilder().withId(1L).build(),
                new MicroserviceBuilder().withId(2L).build()
            );
    }

    @Test
    void create_Success() {
        given(graphLoaderService.loadGraph()).willReturn(
            GraphUtils.createGraph(
                String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3; 4;",
                    "1 -> 2;",
                    "2 -> 3;}"
                )
            )
        );

        final Dependency expected = dependency(1, 3, 4);

        given(repository.save(any(Dependency.class))).willReturn(expected);

        Dependency persistent = service.create(dependency(null, 3, 4));

        assertThat(persistent).isEqualTo(expected);

        then(repository).should().save(any(Dependency.class));
    }

    @Test
    void update_NoId_ExceptionIsThrown() {
        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                service.update(dependency(null, 1, 2)))
            .withMessageStartingWith("Updating non-persistent entity without id");
    }

    @Test
    void update_SelfCycle_ExceptionIsThrown() {
        final Dependency dependency = dependency(1, 2, 2);

        assertThatThrownBy(() -> service.update(dependency))
            .isInstanceOf(SelfCircularException.class)
            .hasMessageStartingWith("Source of dependency can't be the same as target");
    }

    @Test
    void update_WillIntroduceCircularDependencies_ExceptionIsThrown() {
        given(graphLoaderService.loadGraph()).willReturn(
            GraphUtils.createGraph(
                String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3; 4;",
                    "1 -> 2;",
                    "2 -> 3;",
                    "3 -> 4;}"  // has id = 3L
                )
            )
        );

        given(repository.findById(3L))
            .willReturn(Optional.of(dependency(3, 3, 4)));

        final Dependency dependency = dependency(3, 3, 1);

        assertThatThrownBy(() -> service.update(dependency))
            .isInstanceOf(CircularDependenciesException.class)
            .hasMessageStartingWith("Circular dependency will be introduced")
            .extracting("cycles", InstanceOfAssertFactories.ITERABLE)
            .extracting("id")
            .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void update_DuplicateWillBeIntroduced_ExceptionIsThrown() {
        given(graphLoaderService.loadGraph()).willReturn(
            GraphUtils.createGraph(
                String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3;",
                    "1 -> 2;",
                    "2 -> 3;}" // has id = 2L
                )
            )
        );

        given(repository.findById(2L))
            .willReturn(Optional.of(dependency(2, 2, 3)));

        final Dependency dependency = dependency(2, 1, 2);


        assertThatThrownBy(() -> service.update(dependency))
            .isInstanceOf(DuplicateDependencyException.class)
            .hasMessageStartingWith("Dependency already exists")
            .extracting("dependency", InstanceOfAssertFactories.type(Dependency.class))
            .extracting(Dependency::getSource, Dependency::getTarget)
            .containsExactlyInAnyOrder(
                new MicroserviceBuilder().withId(1L).build(),
                new MicroserviceBuilder().withId(2L).build()
            );
    }

    @Test
    void update_Success() {
        given(graphLoaderService.loadGraph()).willReturn(
            GraphUtils.createGraph(
                String.join("\n",
                    "strict digraph G { ",
                    "1; 2; 3; 4;",
                    "1 -> 2;",
                    "2 -> 3;", // has it 2L
                    "3 -> 4;}"
                )
            )
        );

        given(repository.findById(2L))
            .willReturn(Optional.of(dependency(2, 2, 3)));

        final Dependency expected = dependency(2, 2, 4);

        given(repository.save(any(Dependency.class))).willReturn(expected);

        Dependency persistent = service.update(dependency(2, 2, 4));

        assertThat(persistent).isEqualTo(expected);

        then(repository).should().save(any(Dependency.class));
    }

}
