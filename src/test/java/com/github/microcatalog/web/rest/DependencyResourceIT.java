package com.github.microcatalog.web.rest;

import com.github.microcatalog.MicrocatalogApp;
import com.github.microcatalog.domain.Dependency;
import com.github.microcatalog.domain.Microservice;
import com.github.microcatalog.repository.DependencyRepository;

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
 * Integration tests for the {@link DependencyResource} REST controller.
 */
@SpringBootTest(classes = MicrocatalogApp.class)
@AutoConfigureMockMvc
@WithMockUser
class DependencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DependencyRepository dependencyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDependencyMockMvc;

    private Dependency dependency;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependency createEntity(EntityManager em) {
        Dependency dependency = new Dependency()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);

        // Add required entity
        Microservice source = MicroserviceResourceIT.createEntity(em, "FIRST");
        Microservice target = MicroserviceResourceIT.createEntity(em, "SECOND");
        em.persist(source);
        em.persist(target);
        em.flush();

        dependency.setSource(source);
        dependency.setTarget(target);
        return dependency;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependency createUpdatedEntity(EntityManager em) {
        Dependency dependency = new Dependency()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Microservice microservice;
        if (TestUtil.findAll(em, Microservice.class).isEmpty()) {
            microservice = MicroserviceResourceIT.createUpdatedEntity(em);
            em.persist(microservice);
            em.flush();
        } else {
            microservice = TestUtil.findAll(em, Microservice.class).get(0);
        }
        dependency.setSource(microservice);
        // Add required entity
        dependency.setTarget(microservice);
        return dependency;
    }

    @BeforeEach
    public void initTest() {
        dependency = createEntity(em);
    }

    @Test
    @Transactional
    void createDependency() throws Exception {
        int databaseSizeBeforeCreate = dependencyRepository.findAll().size();
        // Create the Dependency
        restDependencyMockMvc.perform(post("/api/dependencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dependency)))
            .andExpect(status().isCreated());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeCreate + 1);
        Dependency testDependency = dependencyList.get(dependencyList.size() - 1);
        assertThat(testDependency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDependency.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDependencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dependencyRepository.findAll().size();

        // Create the Dependency with an existing ID
        dependency.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependencyMockMvc.perform(post("/api/dependencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dependency)))
            .andExpect(status().is5xxServerError());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependencyRepository.findAll().size();
        // set the field null
        dependency.setName(null);

        // Create the Dependency, which fails.


        restDependencyMockMvc.perform(post("/api/dependencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dependency)))
            .andExpect(status().isBadRequest());

        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDependencies() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        // Get all the dependencyList
        restDependencyMockMvc.perform(get("/api/dependencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getDependency() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        // Get the dependency
        restDependencyMockMvc.perform(get("/api/dependencies/{id}", dependency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dependency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDependency() throws Exception {
        // Get the dependency
        restDependencyMockMvc.perform(get("/api/dependencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateDependency() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        int databaseSizeBeforeUpdate = dependencyRepository.findAll().size();

        // Update the dependency
        Dependency updatedDependency = dependencyRepository.findById(dependency.getId()).get();
        // Disconnect from session so that the updates on updatedDependency are not directly saved in db
        em.detach(updatedDependency);
        updatedDependency
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restDependencyMockMvc.perform(put("/api/dependencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDependency)))
            .andExpect(status().isOk());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeUpdate);
        Dependency testDependency = dependencyList.get(dependencyList.size() - 1);
        assertThat(testDependency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDependency.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void updateNonExistingDependency() throws Exception {
        int databaseSizeBeforeUpdate = dependencyRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependencyMockMvc.perform(put("/api/dependencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dependency)))
            .andExpect(status().is5xxServerError());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDependency() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        int databaseSizeBeforeDelete = dependencyRepository.findAll().size();

        // Delete the dependency
        restDependencyMockMvc.perform(delete("/api/dependencies/{id}", dependency.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
