package com.ipc.ts.service.impl;

import com.ipc.ts.service.TaskTypeService;
import com.ipc.ts.domain.TaskType;
import com.ipc.ts.repository.TaskTypeRepository;
import com.ipc.ts.service.dto.TaskTypeDTO;
import com.ipc.ts.service.mapper.TaskTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing TaskType.
 */
@Service
@Transactional
public class TaskTypeServiceImpl implements TaskTypeService {

    private final Logger log = LoggerFactory.getLogger(TaskTypeServiceImpl.class);

    private final TaskTypeRepository taskTypeRepository;

    private final TaskTypeMapper taskTypeMapper;

    public TaskTypeServiceImpl(TaskTypeRepository taskTypeRepository, TaskTypeMapper taskTypeMapper) {
        this.taskTypeRepository = taskTypeRepository;
        this.taskTypeMapper = taskTypeMapper;
    }

    /**
     * Save a taskType.
     *
     * @param taskTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TaskTypeDTO save(TaskTypeDTO taskTypeDTO) {
        log.debug("Request to save TaskType : {}", taskTypeDTO);
        TaskType taskType = taskTypeMapper.toEntity(taskTypeDTO);
        taskType = taskTypeRepository.save(taskType);
        return taskTypeMapper.toDto(taskType);
    }

    /**
     * Get all the taskTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskTypes");
        return taskTypeRepository.findAll(pageable)
            .map(taskTypeMapper::toDto);
    }


    /**
     * Get one taskType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TaskTypeDTO> findOne(Long id) {
        log.debug("Request to get TaskType : {}", id);
        return taskTypeRepository.findById(id)
            .map(taskTypeMapper::toDto);
    }

    /**
     * Delete the taskType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskType : {}", id);
        taskTypeRepository.deleteById(id);
    }
}
