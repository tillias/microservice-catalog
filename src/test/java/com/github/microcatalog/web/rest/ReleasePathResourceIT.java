package com.github.microcatalog.web.rest;

import com.github.microcatalog.MicrocatalogApp;
import com.github.microcatalog.domain.ReleasePath;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.ReleasePathRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReleasePathResource} REST controller.
 */
@SpringBootTest(classes = MicrocatalogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReleasePathResourceIT {

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ReleasePathRepository releasePathRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleasePathMockMvc;

    private ReleasePath releasePath;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleasePath createEntity(EntityManager em) {
        ReleasePath releasePath = new ReleasePath()
            .createdOn(DEFAULT_CREATED_ON);
        // Add required entity
        Microservice microservice;
        if (TestUtil.findAll(em, Microservice.class).isEmpty()) {
            microservice = MicroserviceResourceIT.createEntity(em);
            em.persist(microservice);
            em.flush();
        } else {
            microservice = TestUtil.findAll(em, Microservice.class).get(0);
        }
        releasePath.setTarget(microservice);
        return releasePath;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleasePath createUpdatedEntity(EntityManager em) {
        ReleasePath releasePath = new ReleasePath()
            .createdOn(UPDATED_CREATED_ON);
        // Add required entity
        Microservice microservice;
        if (TestUtil.findAll(em, Microservice.class).isEmpty()) {
            microservice = MicroserviceResourceIT.createUpdatedEntity(em);
            em.persist(microservice);
            em.flush();
        } else {
            microservice = TestUtil.findAll(em, Microservice.class).get(0);
        }
        releasePath.setTarget(microservice);
        return releasePath;
    }

    @BeforeEach
    public void initTest() {
        releasePath = createEntity(em);
    }

    @Test
    @Transactional
    public void createReleasePath() throws Exception {
        int databaseSizeBeforeCreate = releasePathRepository.findAll().size();
        // Create the ReleasePath
        restReleasePathMockMvc.perform(post("/api/release-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releasePath)))
            .andExpect(status().isCreated());

        // Validate the ReleasePath in the database
        List<ReleasePath> releasePathList = releasePathRepository.findAll();
        assertThat(releasePathList).hasSize(databaseSizeBeforeCreate + 1);
        ReleasePath testReleasePath = releasePathList.get(releasePathList.size() - 1);
        assertThat(testReleasePath.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    public void createReleasePathWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = releasePathRepository.findAll().size();

        // Create the ReleasePath with an existing ID
        releasePath.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleasePathMockMvc.perform(post("/api/release-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releasePath)))
            .andExpect(status().isBadRequest());

        // Validate the ReleasePath in the database
        List<ReleasePath> releasePathList = releasePathRepository.findAll();
        assertThat(releasePathList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReleasePaths() throws Exception {
        // Initialize the database
        releasePathRepository.saveAndFlush(releasePath);

        // Get all the releasePathList
        restReleasePathMockMvc.perform(get("/api/release-paths?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releasePath.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getReleasePath() throws Exception {
        // Initialize the database
        releasePathRepository.saveAndFlush(releasePath);

        // Get the releasePath
        restReleasePathMockMvc.perform(get("/api/release-paths/{id}", releasePath.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releasePath.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingReleasePath() throws Exception {
        // Get the releasePath
        restReleasePathMockMvc.perform(get("/api/release-paths/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReleasePath() throws Exception {
        // Initialize the database
        releasePathRepository.saveAndFlush(releasePath);

        int databaseSizeBeforeUpdate = releasePathRepository.findAll().size();

        // Update the releasePath
        ReleasePath updatedReleasePath = releasePathRepository.findById(releasePath.getId()).get();
        // Disconnect from session so that the updates on updatedReleasePath are not directly saved in db
        em.detach(updatedReleasePath);
        updatedReleasePath
            .createdOn(UPDATED_CREATED_ON);

        restReleasePathMockMvc.perform(put("/api/release-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedReleasePath)))
            .andExpect(status().isOk());

        // Validate the ReleasePath in the database
        List<ReleasePath> releasePathList = releasePathRepository.findAll();
        assertThat(releasePathList).hasSize(databaseSizeBeforeUpdate);
        ReleasePath testReleasePath = releasePathList.get(releasePathList.size() - 1);
        assertThat(testReleasePath.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingReleasePath() throws Exception {
        int databaseSizeBeforeUpdate = releasePathRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleasePathMockMvc.perform(put("/api/release-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releasePath)))
            .andExpect(status().isBadRequest());

        // Validate the ReleasePath in the database
        List<ReleasePath> releasePathList = releasePathRepository.findAll();
        assertThat(releasePathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReleasePath() throws Exception {
        // Initialize the database
        releasePathRepository.saveAndFlush(releasePath);

        int databaseSizeBeforeDelete = releasePathRepository.findAll().size();

        // Delete the releasePath
        restReleasePathMockMvc.perform(delete("/api/release-paths/{id}", releasePath.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReleasePath> releasePathList = releasePathRepository.findAll();
        assertThat(releasePathList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
