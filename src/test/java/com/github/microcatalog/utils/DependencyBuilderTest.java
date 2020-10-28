package com.github.microcatalog.utils;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyBuilderTest {

    @Test
    void build() {
        DependencyBuilder builder = new DependencyBuilder();
        Dependency dependency = builder
            .withId(1L)
            .withName("test")
            .withSource(2L)
            .withTarget(3L)
            .build();

        assertThat(dependency).isNotNull().extracting(Dependency::getId, Dependency::getName).containsExactly(1L, "test");
        assertThat(dependency.getSource()).isNotNull().extracting(Microservice::getId).isEqualTo(2L);
        assertThat(dependency.getTarget()).isNotNull().extracting(Microservice::getId).isEqualTo(3L);
    }
}
