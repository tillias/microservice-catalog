package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.MockMvcWithUser;
import com.github.microcatalog.service.custom.IntegrationTestsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = IntegrationTestsCustomResource.class)
@MockMvcWithUser
class IntegrationTestsCustomResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IntegrationTestsService service;

    @Test
    void triggerIntegrationTests() throws Exception {

        given(service.triggerIntegrationTests(3L))
            .willReturn(true);

        mockMvc.perform(post("/api/integration-tests/microservice/3")
            .with(csrf()))
            .andExpect(status().isOk());
    }
}
