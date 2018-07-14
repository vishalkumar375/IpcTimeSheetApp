package com.ipc.ts.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ipc.ts.service.ProjectCodeService;
import com.ipc.ts.web.rest.errors.BadRequestAlertException;
import com.ipc.ts.web.rest.util.HeaderUtil;
import com.ipc.ts.web.rest.util.PaginationUtil;
import com.ipc.ts.service.dto.ProjectCodeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProjectCode.
 */
@RestController
@RequestMapping("/api")
public class ProjectCodeResource {

    private final Logger log = LoggerFactory.getLogger(ProjectCodeResource.class);

    private static final String ENTITY_NAME = "projectCode";

    private final ProjectCodeService projectCodeService;

    public ProjectCodeResource(ProjectCodeService projectCodeService) {
        this.projectCodeService = projectCodeService;
    }

    /**
     * POST  /project-codes : Create a new projectCode.
     *
     * @param projectCodeDTO the projectCodeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectCodeDTO, or with status 400 (Bad Request) if the projectCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-codes")
    @Timed
    public ResponseEntity<ProjectCodeDTO> createProjectCode(@Valid @RequestBody ProjectCodeDTO projectCodeDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectCode : {}", projectCodeDTO);
        if (projectCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectCodeDTO result = projectCodeService.save(projectCodeDTO);
        return ResponseEntity.created(new URI("/api/project-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-codes : Updates an existing projectCode.
     *
     * @param projectCodeDTO the projectCodeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectCodeDTO,
     * or with status 400 (Bad Request) if the projectCodeDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectCodeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-codes")
    @Timed
    public ResponseEntity<ProjectCodeDTO> updateProjectCode(@Valid @RequestBody ProjectCodeDTO projectCodeDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectCode : {}", projectCodeDTO);
        if (projectCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProjectCodeDTO result = projectCodeService.save(projectCodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-codes : get all the projectCodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectCodes in body
     */
    @GetMapping("/project-codes")
    @Timed
    public ResponseEntity<List<ProjectCodeDTO>> getAllProjectCodes(Pageable pageable) {
        log.debug("REST request to get a page of ProjectCodes");
        Page<ProjectCodeDTO> page = projectCodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-codes/:id : get the "id" projectCode.
     *
     * @param id the id of the projectCodeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectCodeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-codes/{id}")
    @Timed
    public ResponseEntity<ProjectCodeDTO> getProjectCode(@PathVariable Long id) {
        log.debug("REST request to get ProjectCode : {}", id);
        Optional<ProjectCodeDTO> projectCodeDTO = projectCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectCodeDTO);
    }

    /**
     * DELETE  /project-codes/:id : delete the "id" projectCode.
     *
     * @param id the id of the projectCodeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-codes/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectCode(@PathVariable Long id) {
        log.debug("REST request to delete ProjectCode : {}", id);
        projectCodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
