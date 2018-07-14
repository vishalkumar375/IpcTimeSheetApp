package com.ipc.ts.web.rest;

import com.ipc.ts.IpcTimeSheetApp;

import com.ipc.ts.domain.TaskType;
import com.ipc.ts.repository.TaskTypeRepository;
import com.ipc.ts.service.TaskTypeService;
import com.ipc.ts.service.dto.TaskTypeDTO;
import com.ipc.ts.service.mapper.TaskTypeMapper;
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
 * Test class for the TaskTypeResource REST controller.
 *
 * @see TaskTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IpcTimeSheetApp.class)
public class TaskTypeResourceIntTest {

    private static final String DEFAULT_TASK_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TASK_TYPE = "BBBBBBBBBB";

    @Autowired
    private TaskTypeRepository taskTypeRepository;


    @Autowired
    private TaskTypeMapper taskTypeMapper;
    

    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskTypeMockMvc;

    private TaskType taskType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskTypeResource taskTypeResource = new TaskTypeResource(taskTypeService);
        this.restTaskTypeMockMvc = MockMvcBuilders.standaloneSetup(taskTypeResource)
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
    public static TaskType createEntity(EntityManager em) {
        TaskType taskType = new TaskType()
            .taskType(DEFAULT_TASK_TYPE);
        return taskType;
    }

    @Before
    public void initTest() {
        taskType = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskType() throws Exception {
        int databaseSizeBeforeCreate = taskTypeRepository.findAll().size();

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);
        restTaskTypeMockMvc.perform(post("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TaskType testTaskType = taskTypeList.get(taskTypeList.size() - 1);
        assertThat(testTaskType.getTaskType()).isEqualTo(DEFAULT_TASK_TYPE);
    }

    @Test
    @Transactional
    public void createTaskTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskTypeRepository.findAll().size();

        // Create the TaskType with an existing ID
        taskType.setId(1L);
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskTypeMockMvc.perform(post("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTaskTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTypeRepository.findAll().size();
        // set the field null
        taskType.setTaskType(null);

        // Create the TaskType, which fails.
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        restTaskTypeMockMvc.perform(post("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskTypes() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList
        restTaskTypeMockMvc.perform(get("/api/task-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskType.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getTaskType() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/task-types/{id}", taskType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskType.getId().intValue()))
            .andExpect(jsonPath("$.taskType").value(DEFAULT_TASK_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingTaskType() throws Exception {
        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/task-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskType() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        int databaseSizeBeforeUpdate = taskTypeRepository.findAll().size();

        // Update the taskType
        TaskType updatedTaskType = taskTypeRepository.findById(taskType.getId()).get();
        // Disconnect from session so that the updates on updatedTaskType are not directly saved in db
        em.detach(updatedTaskType);
        updatedTaskType
            .taskType(UPDATED_TASK_TYPE);
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(updatedTaskType);

        restTaskTypeMockMvc.perform(put("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTypeDTO)))
            .andExpect(status().isOk());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeUpdate);
        TaskType testTaskType = taskTypeList.get(taskTypeList.size() - 1);
        assertThat(testTaskType.getTaskType()).isEqualTo(UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskType() throws Exception {
        int databaseSizeBeforeUpdate = taskTypeRepository.findAll().size();

        // Create the TaskType
        TaskTypeDTO taskTypeDTO = taskTypeMapper.toDto(taskType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskTypeMockMvc.perform(put("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTaskType() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        int databaseSizeBeforeDelete = taskTypeRepository.findAll().size();

        // Get the taskType
        restTaskTypeMockMvc.perform(delete("/api/task-types/{id}", taskType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskType.class);
        TaskType taskType1 = new TaskType();
        taskType1.setId(1L);
        TaskType taskType2 = new TaskType();
        taskType2.setId(taskType1.getId());
        assertThat(taskType1).isEqualTo(taskType2);
        taskType2.setId(2L);
        assertThat(taskType1).isNotEqualTo(taskType2);
        taskType1.setId(null);
        assertThat(taskType1).isNotEqualTo(taskType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskTypeDTO.class);
        TaskTypeDTO taskTypeDTO1 = new TaskTypeDTO();
        taskTypeDTO1.setId(1L);
        TaskTypeDTO taskTypeDTO2 = new TaskTypeDTO();
        assertThat(taskTypeDTO1).isNotEqualTo(taskTypeDTO2);
        taskTypeDTO2.setId(taskTypeDTO1.getId());
        assertThat(taskTypeDTO1).isEqualTo(taskTypeDTO2);
        taskTypeDTO2.setId(2L);
        assertThat(taskTypeDTO1).isNotEqualTo(taskTypeDTO2);
        taskTypeDTO1.setId(null);
        assertThat(taskTypeDTO1).isNotEqualTo(taskTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taskTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(taskTypeMapper.fromId(null)).isNull();
    }
}
