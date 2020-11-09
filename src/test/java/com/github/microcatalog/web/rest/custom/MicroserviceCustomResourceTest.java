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

import static com.github.microcatalog.service.dto.custom.builder.MicroserviceDtoBuilder.aMicroserviceDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    void deleteByName_Success() throws Exception {
        final String name = "Name";
        given(service.deleteByName(name)).willReturn(4L);

        mockMvc.perform(delete("/api/microservices/by/name/{name}", name)
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").value(4L));
    }

    @Test
    void findAllById_Found_Success() throws Exception {

        given(service.findAllById(Arrays.asList(1L, 2L, 3L)))
            .willReturn(Arrays.asList(
                aMicroserviceDto().withId(1L).build(),
                aMicroserviceDto().withId(2L).build(),
                aMicroserviceDto().withId(3L).build())
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
