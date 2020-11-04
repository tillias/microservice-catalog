package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.MockMvcWithUser;
import com.github.microcatalog.domain.custom.impact.analysis.Result;
import com.github.microcatalog.service.custom.ImpactAnalysisService;
import com.github.microcatalog.utils.MicroserviceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ImpactAnalysisCustomResource.class)
@MockMvcWithUser
class ImpactAnalysisCustomResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpactAnalysisService service;

    @Test
    void calculateImpactAnalysisResult() throws Exception {

        given(service.calculate(3L))
            .willReturn(Optional.of(
                new Result().target(new MicroserviceBuilder().withId(3L).build())));

        mockMvc.perform(get("/api/impact-analysis/microservice/3"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.target.id").value(3L));
    }
}
