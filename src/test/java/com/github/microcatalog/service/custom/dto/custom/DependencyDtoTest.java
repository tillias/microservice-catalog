package com.github.microcatalog.service.custom.dto.custom;

import com.github.microcatalog.service.dto.custom.DependencyDto;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class DependencyDtoTest {

    @Test
    void testEquals() {
        EqualsVerifier.forClass(DependencyDto.class)
            .withRedefinedSuperclass()
            .withOnlyTheseFields("id")
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
}
