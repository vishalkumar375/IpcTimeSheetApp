package com.ipc.ts.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ipc.ts.domain.Organization;
import com.ipc.ts.domain.*; // for static metamodels
import com.ipc.ts.repository.OrganizationRepository;
import com.ipc.ts.service.dto.OrganizationCriteria;

import com.ipc.ts.service.dto.OrganizationDTO;
import com.ipc.ts.service.mapper.OrganizationMapper;

/**
 * Service for executing complex queries for Organization entities in the database.
 * The main input is a {@link OrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationDTO} or a {@link Page} of {@link OrganizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationQueryService extends QueryService<Organization> {

    private final Logger log = LoggerFactory.getLogger(OrganizationQueryService.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    public OrganizationQueryService(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    /**
     * Return a {@link List} of {@link OrganizationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findByCriteria(OrganizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationMapper.toDto(organizationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrganizationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findByCriteria(OrganizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.findAll(specification, page)
            .map(organizationMapper::toDto);
    }

    /**
     * Function to convert OrganizationCriteria to a {@link Specification}
     */
    private Specification<Organization> createSpecification(OrganizationCriteria criteria) {
        Specification<Organization> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Organization_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Organization_.name));
            }
        }
        return specification;
    }

}
