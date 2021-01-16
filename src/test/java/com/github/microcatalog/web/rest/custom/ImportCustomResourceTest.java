package com.github.microcatalog.web.rest.custom;

import com.github.microcatalog.MockMvcWithUser;
import com.github.microcatalog.service.custom.ImportService;
import com.github.microcatalog.service.custom.exceptions.ImportException;
import com.github.microcatalog.service.dto.custom.MicroserviceImportDescriptorDto;
import com.github.microcatalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.github.microcatalog.service.dto.custom.builder.FullMicroserviceDtoBuilder.aFullMicroserviceDto;
import static com.github.microcatalog.service.dto.custom.builder.MicroserviceImportDescriptorDtoBuilder.aMicroserviceImportDescriptorDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ImportCustomResource.class)
@MockMvcWithUser
class ImportCustomResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportService service;

    @Test
    void import_MissingName_ReturnsImportException() throws Exception {

        final MicroserviceImportDescriptorDto request = aMicroserviceImportDescriptorDto()
            .withName("Import")
            .build();

        given(service.importFromDescriptor(any())).willReturn(Optional.of(aFullMicroserviceDto()
            .withId(3L)
            .build()
        ));

        mockMvc.perform(post("/api/import")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(3L));
    }
}
