package com.github.microcatalog.service.custom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GraphOperationsServiceTest {

    @Test
    void graphContext_NullGraph() {
        final GraphOperationsService.GraphContext context = new GraphOperationsService.GraphContext(null, null);
        assertThat(context.hasEmptyGraph()).isTrue();
    }
}
