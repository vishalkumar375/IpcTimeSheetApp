package com.ipc.ts.service.impl;

import com.ipc.ts.service.AgileTeamService;
import com.ipc.ts.domain.AgileTeam;
import com.ipc.ts.repository.AgileTeamRepository;
import com.ipc.ts.service.dto.AgileTeamDTO;
import com.ipc.ts.service.mapper.AgileTeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing AgileTeam.
 */
@Service
@Transactional
public class AgileTeamServiceImpl implements AgileTeamService {

    private final Logger log = LoggerFactory.getLogger(AgileTeamServiceImpl.class);

    private final AgileTeamRepository agileTeamRepository;

    private final AgileTeamMapper agileTeamMapper;

    public AgileTeamServiceImpl(AgileTeamRepository agileTeamRepository, AgileTeamMapper agileTeamMapper) {
        this.agileTeamRepository = agileTeamRepository;
        this.agileTeamMapper = agileTeamMapper;
    }

    /**
     * Save a agileTeam.
     *
     * @param agileTeamDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AgileTeamDTO save(AgileTeamDTO agileTeamDTO) {
        log.debug("Request to save AgileTeam : {}", agileTeamDTO);
        AgileTeam agileTeam = agileTeamMapper.toEntity(agileTeamDTO);
        agileTeam = agileTeamRepository.save(agileTeam);
        return agileTeamMapper.toDto(agileTeam);
    }

    /**
     * Get all the agileTeams.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgileTeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgileTeams");
        return agileTeamRepository.findAll(pageable)
            .map(agileTeamMapper::toDto);
    }


    /**
     * Get one agileTeam by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AgileTeamDTO> findOne(Long id) {
        log.debug("Request to get AgileTeam : {}", id);
        return agileTeamRepository.findById(id)
            .map(agileTeamMapper::toDto);
    }

    /**
     * Delete the agileTeam by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgileTeam : {}", id);
        agileTeamRepository.deleteById(id);
    }
}
