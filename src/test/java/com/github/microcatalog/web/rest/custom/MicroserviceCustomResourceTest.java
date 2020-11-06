package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.MockMvcWithUser;
import com.github.microcatalog.service.custom.MicroserviceService;
import com.github.microcatalog.service.dto.custom.MicroserviceDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MicroserviceCustomResource.class)
@MockMvcWithUser
class MicroserviceCustomResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MicroserviceService service;

    @Test
    void findAllById_Found_Success() throws Exception {

        given(service.findAllById(Arrays.asList(1L, 2L, 3L)))
            .willReturn(Arrays.asList(
                new MicroserviceDto().id(1L),
                new MicroserviceDto().id(2L),
                new MicroserviceDto().id(3L))
            );

        mockMvc.perform(get("/api/microservices/by/1,2,3"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[0].id").value(1L))
            .andExpect(jsonPath("$.[1].id").value(2L))
            .andExpect(jsonPath("$.[2].id").value(3L));
    }

    @Test
    void findAllById_NotFound_NoException() throws Exception {
        given(service.findAllById(any()))
            .willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/microservices/by/1,2,3"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", Matchers.empty()));
    }
}
