package com.ipc.ts.web.rest;

import com.ipc.ts.IpcTimeSheetApp;

import com.ipc.ts.domain.ProjectCode;
import com.ipc.ts.repository.ProjectCodeRepository;
import com.ipc.ts.service.ProjectCodeService;
import com.ipc.ts.service.dto.ProjectCodeDTO;
import com.ipc.ts.service.mapper.ProjectCodeMapper;
import com.ipc.ts.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.ipc.ts.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectCodeResource REST controller.
 *
 * @see ProjectCodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpcTimeSheetApp.class)
public class ProjectCodeResourceIntTest {

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    @Autowired
    private ProjectCodeRepository projectCodeRepository;


    @Autowired
    private ProjectCodeMapper projectCodeMapper;
    

    @Autowired
    private ProjectCodeService projectCodeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectCodeMockMvc;

    private ProjectCode projectCode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectCodeResource projectCodeResource = new ProjectCodeResource(projectCodeService);
        this.restProjectCodeMockMvc = MockMvcBuilders.standaloneSetup(projectCodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectCode createEntity(EntityManager em) {
        ProjectCode projectCode = new ProjectCode()
            .projectCode(DEFAULT_PROJECT_CODE);
        return projectCode;
    }

    @Before
    public void initTest() {
        projectCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectCode() throws Exception {
        int databaseSizeBeforeCreate = projectCodeRepository.findAll().size();

        // Create the ProjectCode
        ProjectCodeDTO projectCodeDTO = projectCodeMapper.toDto(projectCode);
        restProjectCodeMockMvc.perform(post("/api/project-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectCode in the database
        List<ProjectCode> projectCodeList = projectCodeRepository.findAll();
        assertThat(projectCodeList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectCode testProjectCode = projectCodeList.get(projectCodeList.size() - 1);
        assertThat(testProjectCode.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
    }

    @Test
    @Transactional
    public void createProjectCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectCodeRepository.findAll().size();

        // Create the ProjectCode with an existing ID
        projectCode.setId(1L);
        ProjectCodeDTO projectCodeDTO = projectCodeMapper.toDto(projectCode);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectCodeMockMvc.perform(post("/api/project-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectCode in the database
        List<ProjectCode> projectCodeList = projectCodeRepository.findAll();
        assertThat(projectCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectCodeRepository.findAll().size();
        // set the field null
        projectCode.setProjectCode(null);

        // Create the ProjectCode, which fails.
        ProjectCodeDTO projectCodeDTO = projectCodeMapper.toDto(projectCode);

        restProjectCodeMockMvc.perform(post("/api/project-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectCodeDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectCode> projectCodeList = projectCodeRepository.findAll();
        assertThat(projectCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectCodes() throws Exception {
        // Initialize the database
        projectCodeRepository.saveAndFlush(projectCode);

        // Get all the projectCodeList
        restProjectCodeMockMvc.perform(get("/api/project-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())));
    }
    

    @Test
    @Transactional
    public void getProjectCode() throws Exception {
        // Initialize the database
        projectCodeRepository.saveAndFlush(projectCode);

        // Get the projectCode
        restProjectCodeMockMvc.perform(get("/api/project-codes/{id}", projectCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectCode.getId().intValue()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingProjectCode() throws Exception {
        // Get the projectCode
        restProjectCodeMockMvc.perform(get("/api/project-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectCode() throws Exception {
        // Initialize the database
        projectCodeRepository.saveAndFlush(projectCode);

        int databaseSizeBeforeUpdate = projectCodeRepository.findAll().size();

        // Update the projectCode
        ProjectCode updatedProjectCode = projectCodeRepository.findById(projectCode.getId()).get();
        // Disconnect from session so that the updates on updatedProjectCode are not directly saved in db
        em.detach(updatedProjectCode);
        updatedProjectCode
            .projectCode(UPDATED_PROJECT_CODE);
        ProjectCodeDTO projectCodeDTO = projectCodeMapper.toDto(updatedProjectCode);

        restProjectCodeMockMvc.perform(put("/api/project-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectCodeDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectCode in the database
        List<ProjectCode> projectCodeList = projectCodeRepository.findAll();
        assertThat(projectCodeList).hasSize(databaseSizeBeforeUpdate);
        ProjectCode testProjectCode = projectCodeList.get(projectCodeList.size() - 1);
        assertThat(testProjectCode.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectCode() throws Exception {
        int databaseSizeBeforeUpdate = projectCodeRepository.findAll().size();

        // Create the ProjectCode
        ProjectCodeDTO projectCodeDTO = projectCodeMapper.toDto(projectCode);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectCodeMockMvc.perform(put("/api/project-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectCode in the database
        List<ProjectCode> projectCodeList = projectCodeRepository.findAll();
        assertThat(projectCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProjectCode() throws Exception {
        // Initialize the database
        projectCodeRepository.saveAndFlush(projectCode);

        int databaseSizeBeforeDelete = projectCodeRepository.findAll().size();

        // Get the projectCode
        restProjectCodeMockMvc.perform(delete("/api/project-codes/{id}", projectCode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectCode> projectCodeList = projectCodeRepository.findAll();
        assertThat(projectCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectCode.class);
        ProjectCode projectCode1 = new ProjectCode();
        projectCode1.setId(1L);
        ProjectCode projectCode2 = new ProjectCode();
        projectCode2.setId(projectCode1.getId());
        assertThat(projectCode1).isEqualTo(projectCode2);
        projectCode2.setId(2L);
        assertThat(projectCode1).isNotEqualTo(projectCode2);
        projectCode1.setId(null);
        assertThat(projectCode1).isNotEqualTo(projectCode2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectCodeDTO.class);
        ProjectCodeDTO projectCodeDTO1 = new ProjectCodeDTO();
        projectCodeDTO1.setId(1L);
        ProjectCodeDTO projectCodeDTO2 = new ProjectCodeDTO();
        assertThat(projectCodeDTO1).isNotEqualTo(projectCodeDTO2);
        projectCodeDTO2.setId(projectCodeDTO1.getId());
        assertThat(projectCodeDTO1).isEqualTo(projectCodeDTO2);
        projectCodeDTO2.setId(2L);
        assertThat(projectCodeDTO1).isNotEqualTo(projectCodeDTO2);
        projectCodeDTO1.setId(null);
        assertThat(projectCodeDTO1).isNotEqualTo(projectCodeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectCodeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectCodeMapper.fromId(null)).isNull();
    }
}
