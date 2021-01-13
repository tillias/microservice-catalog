package com.github.microcatalog.service.custom;

import com.github.microcatalog.config.ApplicationProperties;
import com.github.microcatalog.service.custom.exceptions.ImportException;
import com.github.microcatalog.service.dto.custom.FullMicroserviceDto;
import com.github.microcatalog.service.dto.custom.MicroserviceImportDescriptorDto;
import com.github.microcatalog.service.dto.custom.TeamDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.microcatalog.service.dto.custom.builder.MicroserviceImportDescriptorDtoBuilder.aMicroserviceImportDescriptorDto;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ImportServiceTest {

    @Autowired
    private ImportService sut;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Test
    void importFromDescriptor_missingName_Exception() {
        final MicroserviceImportDescriptorDto descriptorDto = aMicroserviceImportDescriptorDto()
            .withName(null)
            .build();

        assertThatThrownBy(() -> sut.importFromDescriptor(descriptorDto)).isInstanceOf(ImportException.class)
            .hasMessageStartingWith("Microservice name can't be null");
    }

    @Test
    void importFromDescriptor_noDependencies_Success() {
        final MicroserviceImportDescriptorDto descriptorDto =
            aMicroserviceImportDescriptorDto()
                .withName("Test Imported Microservice")
                .build();

        final Optional<FullMicroserviceDto> maybeDto = sut.importFromDescriptor(descriptorDto);
        assertTrue(maybeDto.isPresent());

        final FullMicroserviceDto dto = maybeDto.get();
        assertNotNull(dto);
        assertEquals(descriptorDto.getName(), dto.getName());

        ApplicationProperties.Imports.Defaults defaults = applicationProperties.getImports().getDefaults();
        assertEquals(defaults.getDescription(), dto.getDescription());
        assertEquals(defaults.getImageUrl(), dto.getImageUrl());
        assertEquals(defaults.getApiUrl(), dto.getSwaggerUrl());
        assertEquals(defaults.getGitUrl(), dto.getGitUrl());
        assertEquals(defaults.getCiUrl(), dto.getCiUrl());
        assertNotNull(dto.getStatus());
        assertEquals(defaults.getStatus(), dto.getStatus().getName());
        assertNotNull(dto.getTeam());

        final TeamDto team = dto.getTeam();
        assertEquals(defaults.getTeam().getName(), team.getName());
    }

    @Test
    void importFromDescriptor_overrideDefaultValues_Success() {
        final MicroserviceImportDescriptorDto descriptorDto =
            aMicroserviceImportDescriptorDto()
                .withName("Test Imported Microservice 2")
                .withDescription("Test description")
                .withImageUrl("Test image url")
                .withApiUrl("Test api url")
                .withGitUrl("Test git url")
                .withCiUrl("Test ci url")
                .withTeam("Test team")
                .withStatus("Test status")
                .build();

        final Optional<FullMicroserviceDto> maybeDto = sut.importFromDescriptor(descriptorDto);
        assertTrue(maybeDto.isPresent());

        final FullMicroserviceDto dto = maybeDto.get();
        assertEquals(descriptorDto.getDescription(), dto.getDescription());
        assertEquals(descriptorDto.getImageUrl(), dto.getImageUrl());
        assertEquals(descriptorDto.getApiUrl(), dto.getSwaggerUrl());
        assertEquals(descriptorDto.getGitUrl(), dto.getGitUrl());
        assertEquals(descriptorDto.getCiUrl(), dto.getCiUrl());
        assertNotNull(dto.getStatus());
        assertEquals(descriptorDto.getStatus(), dto.getStatus().getName());
        assertNotNull(dto.getTeam());

        final TeamDto team = dto.getTeam();
        assertEquals(descriptorDto.getTeam(), team.getName());
    }

}
