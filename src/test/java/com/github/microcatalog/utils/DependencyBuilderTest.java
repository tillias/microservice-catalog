package com.github.microcatalog.utils;

import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyBuilderTest {

    @Test
    void build() {
        DependencyBuilder builder = new DependencyBuilder();
        Dependency dependency = builder.withId(1L).withSource(2L).withTarget(3L).build();
        assertThat(dependency).isNotNull().extracting(Dependency::getId).isEqualTo(1L);
        assertThat(dependency.getSource()).isNotNull().extracting(Microservice::getId).isEqualTo(2L);
        assertThat(dependency.getTarget()).isNotNull().extracting(Microservice::getId).isEqualTo(3L);
    }
}
