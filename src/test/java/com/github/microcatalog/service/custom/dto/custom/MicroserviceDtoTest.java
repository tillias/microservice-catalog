package com.github.microcatalog.service.custom.dto.custom;

import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class MicroserviceDtoTest {

    @Test
    void testEquals() {
        EqualsVerifier.forClass(MicroserviceDto.class)
            .withRedefinedSuperclass()
            .withOnlyTheseFields("id")
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
}
