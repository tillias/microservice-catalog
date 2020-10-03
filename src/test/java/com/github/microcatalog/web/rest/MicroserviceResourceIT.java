package com.github.microcatalog.web.rest;

import com.github.microcatalog.MicrocatalogApp;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.domain.Team;
import com.github.microcatalog.domain.Status;
import com.github.microcatalog.repository.MicroserviceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MicroserviceResource} REST controller.
 */
@SpringBootTest(classes = MicrocatalogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MicroserviceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SWAGGER_URL = "AAAAAAAAAA";
    private static final String UPDATED_SWAGGER_URL = "BBBBBBBBBB";

    private static final String DEFAULT_GIT_URL = "AAAAAAAAAA";
    private static final String UPDATED_GIT_URL = "BBBBBBBBBB";

    @Autowired
    private MicroserviceRepository microserviceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMicroserviceMockMvc;

    private Microservice microservice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Microservice createEntity(EntityManager em) {
        Microservice microservice = new Microservice()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .imageUrl(DEFAULT_IMAGE_URL)
            .swaggerUrl(DEFAULT_SWAGGER_URL)
            .gitUrl(DEFAULT_GIT_URL);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        microservice.setTeam(team);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createEntity(em);
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).get(0);
        }
        microservice.setStatus(status);
        return microservice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Microservice createUpdatedEntity(EntityManager em) {
        Microservice microservice = new Microservice()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .swaggerUrl(UPDATED_SWAGGER_URL)
            .gitUrl(UPDATED_GIT_URL);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        microservice.setTeam(team);
        // Add required entity
        Status status;
        if (TestUtil.findAll(em, Status.class).isEmpty()) {
            status = StatusResourceIT.createUpdatedEntity(em);
            em.persist(status);
            em.flush();
        } else {
            status = TestUtil.findAll(em, Status.class).get(0);
        }
        microservice.setStatus(status);
        return microservice;
    }

    @BeforeEach
    public void initTest() {
        microservice = createEntity(em);
    }

    @Test
    @Transactional
    public void createMicroservice() throws Exception {
        int databaseSizeBeforeCreate = microserviceRepository.findAll().size();
        // Create the Microservice
        restMicroserviceMockMvc.perform(post("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(microservice)))
            .andExpect(status().isCreated());

        // Validate the Microservice in the database
        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeCreate + 1);
        Microservice testMicroservice = microserviceList.get(microserviceList.size() - 1);
        assertThat(testMicroservice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMicroservice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMicroservice.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testMicroservice.getSwaggerUrl()).isEqualTo(DEFAULT_SWAGGER_URL);
        assertThat(testMicroservice.getGitUrl()).isEqualTo(DEFAULT_GIT_URL);
    }

    @Test
    @Transactional
    public void createMicroserviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = microserviceRepository.findAll().size();

        // Create the Microservice with an existing ID
        microservice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMicroserviceMockMvc.perform(post("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(microservice)))
            .andExpect(status().isBadRequest());

        // Validate the Microservice in the database
        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceRepository.findAll().size();
        // set the field null
        microservice.setName(null);

        // Create the Microservice, which fails.


        restMicroserviceMockMvc.perform(post("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(microservice)))
            .andExpect(status().isBadRequest());

        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceRepository.findAll().size();
        // set the field null
        microservice.setImageUrl(null);

        // Create the Microservice, which fails.


        restMicroserviceMockMvc.perform(post("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(microservice)))
            .andExpect(status().isBadRequest());

        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSwaggerUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceRepository.findAll().size();
        // set the field null
        microservice.setSwaggerUrl(null);

        // Create the Microservice, which fails.


        restMicroserviceMockMvc.perform(post("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(microservice)))
            .andExpect(status().isBadRequest());

        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGitUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = microserviceRepository.findAll().size();
        // set the field null
        microservice.setGitUrl(null);

        // Create the Microservice, which fails.


        restMicroserviceMockMvc.perform(post("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(microservice)))
            .andExpect(status().isBadRequest());

        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMicroservices() throws Exception {
        // Initialize the database
        microserviceRepository.saveAndFlush(microservice);

        // Get all the microserviceList
        restMicroserviceMockMvc.perform(get("/api/microservices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microservice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].swaggerUrl").value(hasItem(DEFAULT_SWAGGER_URL)))
            .andExpect(jsonPath("$.[*].gitUrl").value(hasItem(DEFAULT_GIT_URL)));
    }
    
    @Test
    @Transactional
    public void getMicroservice() throws Exception {
        // Initialize the database
        microserviceRepository.saveAndFlush(microservice);

        // Get the microservice
        restMicroserviceMockMvc.perform(get("/api/microservices/{id}", microservice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(microservice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.swaggerUrl").value(DEFAULT_SWAGGER_URL))
            .andExpect(jsonPath("$.gitUrl").value(DEFAULT_GIT_URL));
    }
    @Test
    @Transactional
    public void getNonExistingMicroservice() throws Exception {
        // Get the microservice
        restMicroserviceMockMvc.perform(get("/api/microservices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMicroservice() throws Exception {
        // Initialize the database
        microserviceRepository.saveAndFlush(microservice);

        int databaseSizeBeforeUpdate = microserviceRepository.findAll().size();

        // Update the microservice
        Microservice updatedMicroservice = microserviceRepository.findById(microservice.getId()).get();
        // Disconnect from session so that the updates on updatedMicroservice are not directly saved in db
        em.detach(updatedMicroservice);
        updatedMicroservice
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .swaggerUrl(UPDATED_SWAGGER_URL)
            .gitUrl(UPDATED_GIT_URL);

        restMicroserviceMockMvc.perform(put("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMicroservice)))
            .andExpect(status().isOk());

        // Validate the Microservice in the database
        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeUpdate);
        Microservice testMicroservice = microserviceList.get(microserviceList.size() - 1);
        assertThat(testMicroservice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMicroservice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMicroservice.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testMicroservice.getSwaggerUrl()).isEqualTo(UPDATED_SWAGGER_URL);
        assertThat(testMicroservice.getGitUrl()).isEqualTo(UPDATED_GIT_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingMicroservice() throws Exception {
        int databaseSizeBeforeUpdate = microserviceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMicroserviceMockMvc.perform(put("/api/microservices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(microservice)))
            .andExpect(status().isBadRequest());

        // Validate the Microservice in the database
        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMicroservice() throws Exception {
        // Initialize the database
        microserviceRepository.saveAndFlush(microservice);

        int databaseSizeBeforeDelete = microserviceRepository.findAll().size();

        // Delete the microservice
        restMicroserviceMockMvc.perform(delete("/api/microservices/{id}", microservice.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Microservice> microserviceList = microserviceRepository.findAll();
        assertThat(microserviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
