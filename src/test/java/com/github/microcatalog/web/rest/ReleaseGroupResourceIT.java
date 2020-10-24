package com.github.microcatalog.web.rest;

import com.github.microcatalog.MicrocatalogApp;
import com.github.microcatalog.domain.ReleaseGroup;
import com.github.microcatalog.repository.ReleaseGroupRepository;

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
 * Integration tests for the {@link ReleaseGroupResource} REST controller.
 */
@SpringBootTest(classes = MicrocatalogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReleaseGroupResourceIT {

    @Autowired
    private ReleaseGroupRepository releaseGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleaseGroupMockMvc;

    private ReleaseGroup releaseGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleaseGroup createEntity(EntityManager em) {
        ReleaseGroup releaseGroup = new ReleaseGroup();
        return releaseGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleaseGroup createUpdatedEntity(EntityManager em) {
        ReleaseGroup releaseGroup = new ReleaseGroup();
        return releaseGroup;
    }

    @BeforeEach
    public void initTest() {
        releaseGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createReleaseGroup() throws Exception {
        int databaseSizeBeforeCreate = releaseGroupRepository.findAll().size();
        // Create the ReleaseGroup
        restReleaseGroupMockMvc.perform(post("/api/release-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releaseGroup)))
            .andExpect(status().isCreated());

        // Validate the ReleaseGroup in the database
        List<ReleaseGroup> releaseGroupList = releaseGroupRepository.findAll();
        assertThat(releaseGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ReleaseGroup testReleaseGroup = releaseGroupList.get(releaseGroupList.size() - 1);
    }

    @Test
    @Transactional
    public void createReleaseGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = releaseGroupRepository.findAll().size();

        // Create the ReleaseGroup with an existing ID
        releaseGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleaseGroupMockMvc.perform(post("/api/release-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releaseGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ReleaseGroup in the database
        List<ReleaseGroup> releaseGroupList = releaseGroupRepository.findAll();
        assertThat(releaseGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReleaseGroups() throws Exception {
        // Initialize the database
        releaseGroupRepository.saveAndFlush(releaseGroup);

        // Get all the releaseGroupList
        restReleaseGroupMockMvc.perform(get("/api/release-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releaseGroup.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getReleaseGroup() throws Exception {
        // Initialize the database
        releaseGroupRepository.saveAndFlush(releaseGroup);

        // Get the releaseGroup
        restReleaseGroupMockMvc.perform(get("/api/release-groups/{id}", releaseGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releaseGroup.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingReleaseGroup() throws Exception {
        // Get the releaseGroup
        restReleaseGroupMockMvc.perform(get("/api/release-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReleaseGroup() throws Exception {
        // Initialize the database
        releaseGroupRepository.saveAndFlush(releaseGroup);

        int databaseSizeBeforeUpdate = releaseGroupRepository.findAll().size();

        // Update the releaseGroup
        ReleaseGroup updatedReleaseGroup = releaseGroupRepository.findById(releaseGroup.getId()).get();
        // Disconnect from session so that the updates on updatedReleaseGroup are not directly saved in db
        em.detach(updatedReleaseGroup);

        restReleaseGroupMockMvc.perform(put("/api/release-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedReleaseGroup)))
            .andExpect(status().isOk());

        // Validate the ReleaseGroup in the database
        List<ReleaseGroup> releaseGroupList = releaseGroupRepository.findAll();
        assertThat(releaseGroupList).hasSize(databaseSizeBeforeUpdate);
        ReleaseGroup testReleaseGroup = releaseGroupList.get(releaseGroupList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingReleaseGroup() throws Exception {
        int databaseSizeBeforeUpdate = releaseGroupRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseGroupMockMvc.perform(put("/api/release-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(releaseGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ReleaseGroup in the database
        List<ReleaseGroup> releaseGroupList = releaseGroupRepository.findAll();
        assertThat(releaseGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReleaseGroup() throws Exception {
        // Initialize the database
        releaseGroupRepository.saveAndFlush(releaseGroup);

        int databaseSizeBeforeDelete = releaseGroupRepository.findAll().size();

        // Delete the releaseGroup
        restReleaseGroupMockMvc.perform(delete("/api/release-groups/{id}", releaseGroup.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReleaseGroup> releaseGroupList = releaseGroupRepository.findAll();
        assertThat(releaseGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
