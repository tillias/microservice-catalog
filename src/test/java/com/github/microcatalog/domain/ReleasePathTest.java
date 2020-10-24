package com.github.microcatalog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.microcatalog.web.rest.TestUtil;

public class ReleasePathTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleasePath.class);
        ReleasePath releasePath1 = new ReleasePath();
        releasePath1.setId(1L);
        ReleasePath releasePath2 = new ReleasePath();
        releasePath2.setId(releasePath1.getId());
        assertThat(releasePath1).isEqualTo(releasePath2);
        releasePath2.setId(2L);
        assertThat(releasePath1).isNotEqualTo(releasePath2);
        releasePath1.setId(null);
        assertThat(releasePath1).isNotEqualTo(releasePath2);
    }
}
