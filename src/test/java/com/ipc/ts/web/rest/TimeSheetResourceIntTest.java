package com.ipc.ts.web.rest;

import com.ipc.ts.IpcTimeSheetApp;

import com.ipc.ts.domain.TimeSheet;
import com.ipc.ts.repository.TimeSheetRepository;
import com.ipc.ts.service.TimeSheetService;
import com.ipc.ts.service.dto.TimeSheetDTO;
import com.ipc.ts.service.mapper.TimeSheetMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.ipc.ts.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TimeSheetResource REST controller.
 *
 * @see TimeSheetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpcTimeSheetApp.class)
public class TimeSheetResourceIntTest {

    private static final Integer DEFAULT_EMP_ID = 1;
    private static final Integer UPDATED_EMP_ID = 2;

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERSON_ID = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_FOR_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FOR_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FOR_DAY = "AAAAAAAAAA";
    private static final String UPDATED_FOR_DAY = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACTUAL_HOURS = 1;
    private static final Integer UPDATED_ACTUAL_HOURS = 2;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private TimeSheetRepository timeSheetRepository;


    @Autowired
    private TimeSheetMapper timeSheetMapper;
    

    @Autowired
    private TimeSheetService timeSheetService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimeSheetMockMvc;

    private TimeSheet timeSheet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimeSheetResource timeSheetResource = new TimeSheetResource(timeSheetService);
        this.restTimeSheetMockMvc = MockMvcBuilders.standaloneSetup(timeSheetResource)
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
    public static TimeSheet createEntity(EntityManager em) {
        TimeSheet timeSheet = new TimeSheet()
            .empID(DEFAULT_EMP_ID)
            .fullName(DEFAULT_FULL_NAME)
            .personId(DEFAULT_PERSON_ID)
            .forDate(DEFAULT_FOR_DATE)
            .forDay(DEFAULT_FOR_DAY)
            .actualHours(DEFAULT_ACTUAL_HOURS)
            .comments(DEFAULT_COMMENTS);
        return timeSheet;
    }

    @Before
    public void initTest() {
        timeSheet = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimeSheet() throws Exception {
        int databaseSizeBeforeCreate = timeSheetRepository.findAll().size();

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);
        restTimeSheetMockMvc.perform(post("/api/time-sheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO)))
            .andExpect(status().isCreated());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeCreate + 1);
        TimeSheet testTimeSheet = timeSheetList.get(timeSheetList.size() - 1);
        assertThat(testTimeSheet.getEmpID()).isEqualTo(DEFAULT_EMP_ID);
        assertThat(testTimeSheet.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testTimeSheet.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testTimeSheet.getForDate()).isEqualTo(DEFAULT_FOR_DATE);
        assertThat(testTimeSheet.getForDay()).isEqualTo(DEFAULT_FOR_DAY);
        assertThat(testTimeSheet.getActualHours()).isEqualTo(DEFAULT_ACTUAL_HOURS);
        assertThat(testTimeSheet.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createTimeSheetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeSheetRepository.findAll().size();

        // Create the TimeSheet with an existing ID
        timeSheet.setId(1L);
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeSheetMockMvc.perform(post("/api/time-sheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmpIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeSheetRepository.findAll().size();
        // set the field null
        timeSheet.setEmpID(null);

        // Create the TimeSheet, which fails.
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        restTimeSheetMockMvc.perform(post("/api/time-sheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO)))
            .andExpect(status().isBadRequest());

        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeSheetRepository.findAll().size();
        // set the field null
        timeSheet.setFullName(null);

        // Create the TimeSheet, which fails.
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        restTimeSheetMockMvc.perform(post("/api/time-sheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO)))
            .andExpect(status().isBadRequest());

        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimeSheets() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        // Get all the timeSheetList
        restTimeSheetMockMvc.perform(get("/api/time-sheets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].empID").value(hasItem(DEFAULT_EMP_ID)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.toString())))
            .andExpect(jsonPath("$.[*].forDate").value(hasItem(DEFAULT_FOR_DATE.toString())))
            .andExpect(jsonPath("$.[*].forDay").value(hasItem(DEFAULT_FOR_DAY.toString())))
            .andExpect(jsonPath("$.[*].actualHours").value(hasItem(DEFAULT_ACTUAL_HOURS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }
    

    @Test
    @Transactional
    public void getTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        // Get the timeSheet
        restTimeSheetMockMvc.perform(get("/api/time-sheets/{id}", timeSheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timeSheet.getId().intValue()))
            .andExpect(jsonPath("$.empID").value(DEFAULT_EMP_ID))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.toString()))
            .andExpect(jsonPath("$.forDate").value(DEFAULT_FOR_DATE.toString()))
            .andExpect(jsonPath("$.forDay").value(DEFAULT_FOR_DAY.toString()))
            .andExpect(jsonPath("$.actualHours").value(DEFAULT_ACTUAL_HOURS))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingTimeSheet() throws Exception {
        // Get the timeSheet
        restTimeSheetMockMvc.perform(get("/api/time-sheets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();

        // Update the timeSheet
        TimeSheet updatedTimeSheet = timeSheetRepository.findById(timeSheet.getId()).get();
        // Disconnect from session so that the updates on updatedTimeSheet are not directly saved in db
        em.detach(updatedTimeSheet);
        updatedTimeSheet
            .empID(UPDATED_EMP_ID)
            .fullName(UPDATED_FULL_NAME)
            .personId(UPDATED_PERSON_ID)
            .forDate(UPDATED_FOR_DATE)
            .forDay(UPDATED_FOR_DAY)
            .actualHours(UPDATED_ACTUAL_HOURS)
            .comments(UPDATED_COMMENTS);
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(updatedTimeSheet);

        restTimeSheetMockMvc.perform(put("/api/time-sheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO)))
            .andExpect(status().isOk());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
        TimeSheet testTimeSheet = timeSheetList.get(timeSheetList.size() - 1);
        assertThat(testTimeSheet.getEmpID()).isEqualTo(UPDATED_EMP_ID);
        assertThat(testTimeSheet.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testTimeSheet.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testTimeSheet.getForDate()).isEqualTo(UPDATED_FOR_DATE);
        assertThat(testTimeSheet.getForDay()).isEqualTo(UPDATED_FOR_DAY);
        assertThat(testTimeSheet.getActualHours()).isEqualTo(UPDATED_ACTUAL_HOURS);
        assertThat(testTimeSheet.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingTimeSheet() throws Exception {
        int databaseSizeBeforeUpdate = timeSheetRepository.findAll().size();

        // Create the TimeSheet
        TimeSheetDTO timeSheetDTO = timeSheetMapper.toDto(timeSheet);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimeSheetMockMvc.perform(put("/api/time-sheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeSheetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        int databaseSizeBeforeDelete = timeSheetRepository.findAll().size();

        // Get the timeSheet
        restTimeSheetMockMvc.perform(delete("/api/time-sheets/{id}", timeSheet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TimeSheet> timeSheetList = timeSheetRepository.findAll();
        assertThat(timeSheetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSheet.class);
        TimeSheet timeSheet1 = new TimeSheet();
        timeSheet1.setId(1L);
        TimeSheet timeSheet2 = new TimeSheet();
        timeSheet2.setId(timeSheet1.getId());
        assertThat(timeSheet1).isEqualTo(timeSheet2);
        timeSheet2.setId(2L);
        assertThat(timeSheet1).isNotEqualTo(timeSheet2);
        timeSheet1.setId(null);
        assertThat(timeSheet1).isNotEqualTo(timeSheet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSheetDTO.class);
        TimeSheetDTO timeSheetDTO1 = new TimeSheetDTO();
        timeSheetDTO1.setId(1L);
        TimeSheetDTO timeSheetDTO2 = new TimeSheetDTO();
        assertThat(timeSheetDTO1).isNotEqualTo(timeSheetDTO2);
        timeSheetDTO2.setId(timeSheetDTO1.getId());
        assertThat(timeSheetDTO1).isEqualTo(timeSheetDTO2);
        timeSheetDTO2.setId(2L);
        assertThat(timeSheetDTO1).isNotEqualTo(timeSheetDTO2);
        timeSheetDTO1.setId(null);
        assertThat(timeSheetDTO1).isNotEqualTo(timeSheetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(timeSheetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(timeSheetMapper.fromId(null)).isNull();
    }
}
