package com.github.microcatalog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.microcatalog.web.rest.TestUtil;

public class ReleaseStepTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleaseStep.class);
        ReleaseStep releaseStep1 = new ReleaseStep();
        releaseStep1.setId(1L);
        ReleaseStep releaseStep2 = new ReleaseStep();
        releaseStep2.setId(releaseStep1.getId());
        assertThat(releaseStep1).isEqualTo(releaseStep2);
        releaseStep2.setId(2L);
        assertThat(releaseStep1).isNotEqualTo(releaseStep2);
        releaseStep1.setId(null);
        assertThat(releaseStep1).isNotEqualTo(releaseStep2);
    }
}
