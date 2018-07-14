package com.ipc.ts.service;

import com.ipc.ts.service.dto.ProjectCodeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ProjectCode.
 */
public interface ProjectCodeService {

    /**
     * Save a projectCode.
     *
     * @param projectCodeDTO the entity to save
     * @return the persisted entity
     */
    ProjectCodeDTO save(ProjectCodeDTO projectCodeDTO);

    /**
     * Get all the projectCodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProjectCodeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" projectCode.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProjectCodeDTO> findOne(Long id);

    /**
     * Delete the "id" projectCode.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
