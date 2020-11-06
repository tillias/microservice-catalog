package com.github.microcatalog.service.custom;

import com.github.microcatalog.MappersConfig;
import com.github.microcatalog.TestUtils;
import com.github.microcatalog.repository.DependencyRepository;
import com.github.microcatalog.repository.MicroserviceRepository;
import com.github.microcatalog.service.dto.custom.DependencyDto;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.microcatalog.TestUtils.microservice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {MicroserviceService.class, MappersConfig.class})
class MicroserviceServiceTest {

    @MockBean
    private MicroserviceRepository repository;

    @Autowired
    private MicroserviceService service;

    @Test
    void findAllById_EmptyList_NoException() {
        given(repository.findAllById(any())).willReturn(Collections.emptyList());

        final List<MicroserviceDto> dtos = service.findAllById(Arrays.asList(1L, 2L, 3L));
        assertThat(dtos).isEmpty();
    }

    @Test
    void findAllById() {
        given(repository.findAllById(Arrays.asList(1L, 2L, 3L))).willReturn(
            Arrays.asList(
                microservice(1),
                microservice(2),
                microservice(3)
            )
        );
        final List<MicroserviceDto> dtos = service.findAllById(Arrays.asList(1L, 2L, 3L));
        assertThat(dtos).hasSize(3).extracting(MicroserviceDto::getId, MicroserviceDto::getName)
            .containsExactlyInAnyOrder(
                tuple(1L, "1"),
                tuple(2L, "2"),
                tuple(3L, "3"));
    }
}
