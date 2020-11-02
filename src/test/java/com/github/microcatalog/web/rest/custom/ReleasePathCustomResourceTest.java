package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.domain.custom.ReleasePath;
import com.github.microcatalog.service.custom.ReleasePathCustomService;
import com.github.microcatalog.utils.MicroserviceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ReleasePathCustomResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReleasePathCustomService service;

    @Test
    void getReleasePath() throws Exception {

        given(service.getReleasePath(3L))
            .willReturn(Optional.of(
                new ReleasePath()
                    .target(new MicroserviceBuilder().withId(3L).build())));

        mockMvc.perform(get("/api/release-path/microservice/3"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.target.id").value(3L));
    }
}
