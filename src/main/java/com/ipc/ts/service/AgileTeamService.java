package com.ipc.ts.service;

import com.ipc.ts.service.dto.AgileTeamDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing AgileTeam.
 */
public interface AgileTeamService {

    /**
     * Save a agileTeam.
     *
     * @param agileTeamDTO the entity to save
     * @return the persisted entity
     */
    AgileTeamDTO save(AgileTeamDTO agileTeamDTO);

    /**
     * Get all the agileTeams.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AgileTeamDTO> findAll(Pageable pageable);


    /**
     * Get the "id" agileTeam.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AgileTeamDTO> findOne(Long id);

    /**
     * Delete the "id" agileTeam.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
