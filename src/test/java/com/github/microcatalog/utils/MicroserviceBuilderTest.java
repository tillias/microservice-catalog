package com.github.microcatalog.utils;

import com.github.microcatalog.domain.Microservice;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MicroserviceBuilderTest {

    @Test
    void build() {
        MicroserviceBuilder builder = new MicroserviceBuilder();
        Microservice microservice = builder.withId(1L).withName("Test microservice").build();
        assertThat(microservice).isNotNull()
            .extracting(Microservice::getId, Microservice::getName).containsExactly(1L, "Test microservice");
    }
}
