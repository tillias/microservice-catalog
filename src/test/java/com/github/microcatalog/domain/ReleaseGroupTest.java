package com.github.microcatalog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.microcatalog.web.rest.TestUtil;

public class ReleaseGroupTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleaseGroup.class);
        ReleaseGroup releaseGroup1 = new ReleaseGroup();
        releaseGroup1.setId(1L);
        ReleaseGroup releaseGroup2 = new ReleaseGroup();
        releaseGroup2.setId(releaseGroup1.getId());
        assertThat(releaseGroup1).isEqualTo(releaseGroup2);
        releaseGroup2.setId(2L);
        assertThat(releaseGroup1).isNotEqualTo(releaseGroup2);
        releaseGroup1.setId(null);
        assertThat(releaseGroup1).isNotEqualTo(releaseGroup2);
    }
}
