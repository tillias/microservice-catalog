package com.github.microcatalog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.microcatalog.web.rest.TestUtil;

public class TeamTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Team.class);
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(team1.getId());
        assertThat(team1).isEqualTo(team2);
        team2.setId(2L);
        assertThat(team1).isNotEqualTo(team2);
        team1.setId(null);
        assertThat(team1).isNotEqualTo(team2);
    }
}
