package com.ipc.ts.service;

import com.ipc.ts.service.dto.TaskTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TaskType.
 */
public interface TaskTypeService {

    /**
     * Save a taskType.
     *
     * @param taskTypeDTO the entity to save
     * @return the persisted entity
     */
    TaskTypeDTO save(TaskTypeDTO taskTypeDTO);

    /**
     * Get all the taskTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaskTypeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" taskType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TaskTypeDTO> findOne(Long id);

    /**
     * Delete the "id" taskType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
