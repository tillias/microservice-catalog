package com.github.microcatalog.service.custom;

import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.service.dto.custom.FullMicroserviceDto;
import com.github.microcatalog.service.dto.custom.MicroserviceImportDescriptorDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.github.microcatalog.service.dto.custom.builder.MicroserviceImportDescriptorDtoBuilder.aMicroserviceImportDescriptorDto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

/**
 * Workaround for https://github.com/spring-projects/spring-boot/issues/7033 so we can't use @SpyBean in {@link ImportServiceTest}
 */
@SpringBootTest
class ImportServiceBorderTest {

    @MockBean
    private MicroserviceRepository repository;

    @Autowired
    private ImportService sut;


    @Test
    void importFromDescriptor_microserviceWithNameAlreadyExists_NoException() {
        final String name = "Existing microservice";
        given(repository.findByName(name)).willReturn(new Microservice());

        final MicroserviceImportDescriptorDto descriptorDto =
            aMicroserviceImportDescriptorDto()
                .withName(name)
                .build();

        final Optional<FullMicroserviceDto> maybeDto = sut.importFromDescriptor(descriptorDto);
        assertFalse(maybeDto.isPresent());
    }

}
