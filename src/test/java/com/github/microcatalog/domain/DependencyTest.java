package com.github.microcatalog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.microcatalog.web.rest.TestUtil;

public class DependencyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dependency.class);
        Dependency dependency1 = new Dependency();
        dependency1.setId(1L);
        Dependency dependency2 = new Dependency();
        dependency2.setId(dependency1.getId());
        assertThat(dependency1).isEqualTo(dependency2);
        dependency2.setId(2L);
        assertThat(dependency1).isNotEqualTo(dependency2);
        dependency1.setId(null);
        assertThat(dependency1).isNotEqualTo(dependency2);
    }
}
