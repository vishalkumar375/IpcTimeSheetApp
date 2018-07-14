package com.ipc.ts.web.rest;

import com.ipc.ts.IpcTimeSheetApp;

import com.ipc.ts.domain.AgileTeam;
import com.ipc.ts.repository.AgileTeamRepository;
import com.ipc.ts.service.AgileTeamService;
import com.ipc.ts.service.dto.AgileTeamDTO;
import com.ipc.ts.service.mapper.AgileTeamMapper;
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
 * Test class for the AgileTeamResource REST controller.
 *
 * @see AgileTeamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpcTimeSheetApp.class)
public class AgileTeamResourceIntTest {

    private static final String DEFAULT_TEAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_NAME = "BBBBBBBBBB";

    @Autowired
    private AgileTeamRepository agileTeamRepository;


    @Autowired
    private AgileTeamMapper agileTeamMapper;
    

    @Autowired
    private AgileTeamService agileTeamService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgileTeamMockMvc;

    private AgileTeam agileTeam;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgileTeamResource agileTeamResource = new AgileTeamResource(agileTeamService);
        this.restAgileTeamMockMvc = MockMvcBuilders.standaloneSetup(agileTeamResource)
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
    public static AgileTeam createEntity(EntityManager em) {
        AgileTeam agileTeam = new AgileTeam()
            .teamName(DEFAULT_TEAM_NAME);
        return agileTeam;
    }

    @Before
    public void initTest() {
        agileTeam = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgileTeam() throws Exception {
        int databaseSizeBeforeCreate = agileTeamRepository.findAll().size();

        // Create the AgileTeam
        AgileTeamDTO agileTeamDTO = agileTeamMapper.toDto(agileTeam);
        restAgileTeamMockMvc.perform(post("/api/agile-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agileTeamDTO)))
            .andExpect(status().isCreated());

        // Validate the AgileTeam in the database
        List<AgileTeam> agileTeamList = agileTeamRepository.findAll();
        assertThat(agileTeamList).hasSize(databaseSizeBeforeCreate + 1);
        AgileTeam testAgileTeam = agileTeamList.get(agileTeamList.size() - 1);
        assertThat(testAgileTeam.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
    }

    @Test
    @Transactional
    public void createAgileTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agileTeamRepository.findAll().size();

        // Create the AgileTeam with an existing ID
        agileTeam.setId(1L);
        AgileTeamDTO agileTeamDTO = agileTeamMapper.toDto(agileTeam);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgileTeamMockMvc.perform(post("/api/agile-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agileTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AgileTeam in the database
        List<AgileTeam> agileTeamList = agileTeamRepository.findAll();
        assertThat(agileTeamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTeamNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = agileTeamRepository.findAll().size();
        // set the field null
        agileTeam.setTeamName(null);

        // Create the AgileTeam, which fails.
        AgileTeamDTO agileTeamDTO = agileTeamMapper.toDto(agileTeam);

        restAgileTeamMockMvc.perform(post("/api/agile-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agileTeamDTO)))
            .andExpect(status().isBadRequest());

        List<AgileTeam> agileTeamList = agileTeamRepository.findAll();
        assertThat(agileTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgileTeams() throws Exception {
        // Initialize the database
        agileTeamRepository.saveAndFlush(agileTeam);

        // Get all the agileTeamList
        restAgileTeamMockMvc.perform(get("/api/agile-teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agileTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getAgileTeam() throws Exception {
        // Initialize the database
        agileTeamRepository.saveAndFlush(agileTeam);

        // Get the agileTeam
        restAgileTeamMockMvc.perform(get("/api/agile-teams/{id}", agileTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agileTeam.getId().intValue()))
            .andExpect(jsonPath("$.teamName").value(DEFAULT_TEAM_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAgileTeam() throws Exception {
        // Get the agileTeam
        restAgileTeamMockMvc.perform(get("/api/agile-teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgileTeam() throws Exception {
        // Initialize the database
        agileTeamRepository.saveAndFlush(agileTeam);

        int databaseSizeBeforeUpdate = agileTeamRepository.findAll().size();

        // Update the agileTeam
        AgileTeam updatedAgileTeam = agileTeamRepository.findById(agileTeam.getId()).get();
        // Disconnect from session so that the updates on updatedAgileTeam are not directly saved in db
        em.detach(updatedAgileTeam);
        updatedAgileTeam
            .teamName(UPDATED_TEAM_NAME);
        AgileTeamDTO agileTeamDTO = agileTeamMapper.toDto(updatedAgileTeam);

        restAgileTeamMockMvc.perform(put("/api/agile-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agileTeamDTO)))
            .andExpect(status().isOk());

        // Validate the AgileTeam in the database
        List<AgileTeam> agileTeamList = agileTeamRepository.findAll();
        assertThat(agileTeamList).hasSize(databaseSizeBeforeUpdate);
        AgileTeam testAgileTeam = agileTeamList.get(agileTeamList.size() - 1);
        assertThat(testAgileTeam.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAgileTeam() throws Exception {
        int databaseSizeBeforeUpdate = agileTeamRepository.findAll().size();

        // Create the AgileTeam
        AgileTeamDTO agileTeamDTO = agileTeamMapper.toDto(agileTeam);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgileTeamMockMvc.perform(put("/api/agile-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agileTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AgileTeam in the database
        List<AgileTeam> agileTeamList = agileTeamRepository.findAll();
        assertThat(agileTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgileTeam() throws Exception {
        // Initialize the database
        agileTeamRepository.saveAndFlush(agileTeam);

        int databaseSizeBeforeDelete = agileTeamRepository.findAll().size();

        // Get the agileTeam
        restAgileTeamMockMvc.perform(delete("/api/agile-teams/{id}", agileTeam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AgileTeam> agileTeamList = agileTeamRepository.findAll();
        assertThat(agileTeamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgileTeam.class);
        AgileTeam agileTeam1 = new AgileTeam();
        agileTeam1.setId(1L);
        AgileTeam agileTeam2 = new AgileTeam();
        agileTeam2.setId(agileTeam1.getId());
        assertThat(agileTeam1).isEqualTo(agileTeam2);
        agileTeam2.setId(2L);
        assertThat(agileTeam1).isNotEqualTo(agileTeam2);
        agileTeam1.setId(null);
        assertThat(agileTeam1).isNotEqualTo(agileTeam2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgileTeamDTO.class);
        AgileTeamDTO agileTeamDTO1 = new AgileTeamDTO();
        agileTeamDTO1.setId(1L);
        AgileTeamDTO agileTeamDTO2 = new AgileTeamDTO();
        assertThat(agileTeamDTO1).isNotEqualTo(agileTeamDTO2);
        agileTeamDTO2.setId(agileTeamDTO1.getId());
        assertThat(agileTeamDTO1).isEqualTo(agileTeamDTO2);
        agileTeamDTO2.setId(2L);
        assertThat(agileTeamDTO1).isNotEqualTo(agileTeamDTO2);
        agileTeamDTO1.setId(null);
        assertThat(agileTeamDTO1).isNotEqualTo(agileTeamDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(agileTeamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(agileTeamMapper.fromId(null)).isNull();
    }
}
