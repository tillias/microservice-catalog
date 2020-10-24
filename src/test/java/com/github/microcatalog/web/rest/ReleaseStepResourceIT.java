package com.github.microcatalog.web.rest;

import com.github.microcatalog.MicrocatalogApp;
import com.github.microcatalog.domain.ReleaseStep;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.ReleaseStepRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReleaseStepResource} REST controller.
 */
@SpringBootTest(classes = MicrocatalogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReleaseStepResourceIT {

    @Autowired
    private ReleaseStepRepository releaseStepRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleaseStepMockMvc;

    private ReleaseStep releaseStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleaseStep createEntity(EntityManager em) {
        ReleaseStep releaseStep = new ReleaseStep();
        // Add required entity
        Microservice microservice;
        if (TestUtil.findAll(em, Microservice.class).isEmpty()) {
            microservice = MicroserviceResourceIT.createEntity(em);
            em.persist(microservice);
            em.flush();
        } else {
            microservice = TestUtil.findAll(em, Microservice.class).get(0);
        }
        releaseStep.setWorkItem(microservice);
        return releaseStep;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleaseStep createUpdatedEntity(EntityManager em) {
        ReleaseStep releaseStep = new ReleaseStep();
        // Add required entity
        Microservice microservice;
        if (TestUtil.findAll(em, Microservice.class).isEmpty()) {
            microservice = MicroserviceResourceIT.createUpdatedEntity(em);
            em.persist(microservice);
            em.flush();
        } else {
            microservice = TestUtil.findAll(em, Microservice.class).get(0);
        }
        releaseStep.setWorkItem(microservice);
        return releaseStep;
    }

    @BeforeEach
    public void initTest() {
        releaseStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createReleaseStep() throws Exception {
        int databaseSizeBeforeCreate = releaseStepRepository.findAll().size();
        // Create the ReleaseStep
        restReleaseStepMockMvc.perform(post("/api/release-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releaseStep)))
            .andExpect(status().isCreated());

        // Validate the ReleaseStep in the database
        List<ReleaseStep> releaseStepList = releaseStepRepository.findAll();
        assertThat(releaseStepList).hasSize(databaseSizeBeforeCreate + 1);
        ReleaseStep testReleaseStep = releaseStepList.get(releaseStepList.size() - 1);
    }

    @Test
    @Transactional
    public void createReleaseStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = releaseStepRepository.findAll().size();

        // Create the ReleaseStep with an existing ID
        releaseStep.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleaseStepMockMvc.perform(post("/api/release-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releaseStep)))
            .andExpect(status().isBadRequest());

        // Validate the ReleaseStep in the database
        List<ReleaseStep> releaseStepList = releaseStepRepository.findAll();
        assertThat(releaseStepList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReleaseSteps() throws Exception {
        // Initialize the database
        releaseStepRepository.saveAndFlush(releaseStep);

        // Get all the releaseStepList
        restReleaseStepMockMvc.perform(get("/api/release-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releaseStep.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getReleaseStep() throws Exception {
        // Initialize the database
        releaseStepRepository.saveAndFlush(releaseStep);

        // Get the releaseStep
        restReleaseStepMockMvc.perform(get("/api/release-steps/{id}", releaseStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releaseStep.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingReleaseStep() throws Exception {
        // Get the releaseStep
        restReleaseStepMockMvc.perform(get("/api/release-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReleaseStep() throws Exception {
        // Initialize the database
        releaseStepRepository.saveAndFlush(releaseStep);

        int databaseSizeBeforeUpdate = releaseStepRepository.findAll().size();

        // Update the releaseStep
        ReleaseStep updatedReleaseStep = releaseStepRepository.findById(releaseStep.getId()).get();
        // Disconnect from session so that the updates on updatedReleaseStep are not directly saved in db
        em.detach(updatedReleaseStep);

        restReleaseStepMockMvc.perform(put("/api/release-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedReleaseStep)))
            .andExpect(status().isOk());

        // Validate the ReleaseStep in the database
        List<ReleaseStep> releaseStepList = releaseStepRepository.findAll();
        assertThat(releaseStepList).hasSize(databaseSizeBeforeUpdate);
        ReleaseStep testReleaseStep = releaseStepList.get(releaseStepList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingReleaseStep() throws Exception {
        int databaseSizeBeforeUpdate = releaseStepRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseStepMockMvc.perform(put("/api/release-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releaseStep)))
            .andExpect(status().isBadRequest());

        // Validate the ReleaseStep in the database
        List<ReleaseStep> releaseStepList = releaseStepRepository.findAll();
        assertThat(releaseStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReleaseStep() throws Exception {
        // Initialize the database
        releaseStepRepository.saveAndFlush(releaseStep);

        int databaseSizeBeforeDelete = releaseStepRepository.findAll().size();

        // Delete the releaseStep
        restReleaseStepMockMvc.perform(delete("/api/release-steps/{id}", releaseStep.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReleaseStep> releaseStepList = releaseStepRepository.findAll();
        assertThat(releaseStepList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
