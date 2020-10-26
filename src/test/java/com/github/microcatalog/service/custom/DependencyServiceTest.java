package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.service.GraphUtils;
import com.github.microcatalog.service.custom.exceptions.CircularDependenciesException;
import com.github.microcatalog.service.custom.exceptions.SelfCircularException;
import com.github.microcatalog.utils.DependencyBuilder;
import com.github.microcatalog.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest(classes = DependencyService.class)
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
        final long id = 3L;

        given(repository.findById(id)).willReturn(Optional.of(new DependencyBuilder().withId(id).build()));

        Optional<Dependency> maybeDependency = service.findById(id);
        assertThat(maybeDependency).isPresent();
        assertThat(maybeDependency.get()).extracting(Dependency::getId).isEqualTo(id);

        then(repository).should().findById(id);
    }

    @Test
    void deleteById() {
        final long id = 3L;

        service.deleteById(id);

        then(repository).should().deleteById(id);
    }

    @Test
    void create_WithId_ExceptionIsThrown() {
        assertThatThrownBy(() -> service.create(new DependencyBuilder().withId(1L).build()))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageStartingWith("A new dependency cannot already have an ID");
    }

    @Test
    void create_SelfCycle_ExceptionIsThrown() {
        assertThatThrownBy(() -> service.create(
            new DependencyBuilder()
                .withSource(2L)
                .withTarget(2L)
                .build()
        ))
            .isInstanceOf(SelfCircularException.class)
            .hasMessageStartingWith("Source id is the same as target id");
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

        assertThatThrownBy(() -> service.create(
            new DependencyBuilder()
                .withSource(4L)
                .withTarget(1L)
                .build()
        ))
            .isInstanceOf(CircularDependenciesException.class)
            .hasMessageStartingWith("Circular dependency will be introduced.");
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

        final Dependency expected = new DependencyBuilder()
            .withId(1L)
            .withSource(3L)
            .withTarget(4L)
            .build();

        given(repository.save(any(Dependency.class))).willReturn(expected);

        Dependency persistent = service.create(
            new DependencyBuilder()
                .withSource(3L)
                .withTarget(4L)
                .build());

        assertThat(persistent).isEqualTo(expected);

        then(repository).should().save(any(Dependency.class));
    }

    @Test
    void update_NoId_ExceptionIsThrown() {
        assertThatThrownBy(() -> service.update(new DependencyBuilder().withSource(1L).withTarget(2L).build()))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageStartingWith("Invalid id");
    }

    @Test
    void update_SelfCycle_ExceptionIsThrown() {
        assertThatThrownBy(() -> service.update(
            new DependencyBuilder()
                .withId(1L)
                .withSource(2L)
                .withTarget(2L)
                .build()
        ))
            .isInstanceOf(SelfCircularException.class)
            .hasMessageStartingWith("Source id is the same as target id");
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
            .willReturn(Optional.of(new DependencyBuilder().withId(3L).withSource(3L).withTarget(4L).build()));

        assertThatThrownBy(() -> service.update(
            new DependencyBuilder()
                .withId(3L)
                .withSource(3L)
                .withTarget(1L)
                .build()
        ))
            .isInstanceOf(CircularDependenciesException.class)
            .hasMessageStartingWith("Circular dependency will be introduced.");
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
            .willReturn(Optional.of(new DependencyBuilder().withId(2L).withSource(2L).withTarget(3L).build()));

        final Dependency expected = new DependencyBuilder()
            .withId(2L)
            .withSource(2L)
            .withTarget(4L)
            .build();

        given(repository.save(any(Dependency.class))).willReturn(expected);

        Dependency persistent = service.update(
            new DependencyBuilder()
                .withId(2L)
                .withSource(2L)
                .withTarget(4L)
                .build());

        assertThat(persistent).isEqualTo(expected);

        then(repository).should().save(any(Dependency.class));
    }
}
