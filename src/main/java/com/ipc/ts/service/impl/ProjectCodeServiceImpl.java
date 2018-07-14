package com.ipc.ts.service.impl;

import com.ipc.ts.service.ProjectCodeService;
import com.ipc.ts.domain.ProjectCode;
import com.ipc.ts.repository.ProjectCodeRepository;
import com.ipc.ts.service.dto.ProjectCodeDTO;
import com.ipc.ts.service.mapper.ProjectCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ProjectCode.
 */
@Service
@Transactional
public class ProjectCodeServiceImpl implements ProjectCodeService {

    private final Logger log = LoggerFactory.getLogger(ProjectCodeServiceImpl.class);

    private final ProjectCodeRepository projectCodeRepository;

    private final ProjectCodeMapper projectCodeMapper;

    public ProjectCodeServiceImpl(ProjectCodeRepository projectCodeRepository, ProjectCodeMapper projectCodeMapper) {
        this.projectCodeRepository = projectCodeRepository;
        this.projectCodeMapper = projectCodeMapper;
    }

    /**
     * Save a projectCode.
     *
     * @param projectCodeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectCodeDTO save(ProjectCodeDTO projectCodeDTO) {
        log.debug("Request to save ProjectCode : {}", projectCodeDTO);
        ProjectCode projectCode = projectCodeMapper.toEntity(projectCodeDTO);
        projectCode = projectCodeRepository.save(projectCode);
        return projectCodeMapper.toDto(projectCode);
    }

    /**
     * Get all the projectCodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectCodes");
        return projectCodeRepository.findAll(pageable)
            .map(projectCodeMapper::toDto);
    }


    /**
     * Get one projectCode by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectCodeDTO> findOne(Long id) {
        log.debug("Request to get ProjectCode : {}", id);
        return projectCodeRepository.findById(id)
            .map(projectCodeMapper::toDto);
    }

    /**
     * Delete the projectCode by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectCode : {}", id);
        projectCodeRepository.deleteById(id);
    }
}
