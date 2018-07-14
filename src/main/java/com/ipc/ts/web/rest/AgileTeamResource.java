package com.ipc.ts.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ipc.ts.service.AgileTeamService;
import com.ipc.ts.web.rest.errors.BadRequestAlertException;
import com.ipc.ts.web.rest.util.HeaderUtil;
import com.ipc.ts.web.rest.util.PaginationUtil;
import com.ipc.ts.service.dto.AgileTeamDTO;
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
 * REST controller for managing AgileTeam.
 */
@RestController
@RequestMapping("/api")
public class AgileTeamResource {

    private final Logger log = LoggerFactory.getLogger(AgileTeamResource.class);

    private static final String ENTITY_NAME = "agileTeam";

    private final AgileTeamService agileTeamService;

    public AgileTeamResource(AgileTeamService agileTeamService) {
        this.agileTeamService = agileTeamService;
    }

    /**
     * POST  /agile-teams : Create a new agileTeam.
     *
     * @param agileTeamDTO the agileTeamDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agileTeamDTO, or with status 400 (Bad Request) if the agileTeam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agile-teams")
    @Timed
    public ResponseEntity<AgileTeamDTO> createAgileTeam(@Valid @RequestBody AgileTeamDTO agileTeamDTO) throws URISyntaxException {
        log.debug("REST request to save AgileTeam : {}", agileTeamDTO);
        if (agileTeamDTO.getId() != null) {
            throw new BadRequestAlertException("A new agileTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgileTeamDTO result = agileTeamService.save(agileTeamDTO);
        return ResponseEntity.created(new URI("/api/agile-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agile-teams : Updates an existing agileTeam.
     *
     * @param agileTeamDTO the agileTeamDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agileTeamDTO,
     * or with status 400 (Bad Request) if the agileTeamDTO is not valid,
     * or with status 500 (Internal Server Error) if the agileTeamDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agile-teams")
    @Timed
    public ResponseEntity<AgileTeamDTO> updateAgileTeam(@Valid @RequestBody AgileTeamDTO agileTeamDTO) throws URISyntaxException {
        log.debug("REST request to update AgileTeam : {}", agileTeamDTO);
        if (agileTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgileTeamDTO result = agileTeamService.save(agileTeamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agileTeamDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agile-teams : get all the agileTeams.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agileTeams in body
     */
    @GetMapping("/agile-teams")
    @Timed
    public ResponseEntity<List<AgileTeamDTO>> getAllAgileTeams(Pageable pageable) {
        log.debug("REST request to get a page of AgileTeams");
        Page<AgileTeamDTO> page = agileTeamService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agile-teams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agile-teams/:id : get the "id" agileTeam.
     *
     * @param id the id of the agileTeamDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agileTeamDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agile-teams/{id}")
    @Timed
    public ResponseEntity<AgileTeamDTO> getAgileTeam(@PathVariable Long id) {
        log.debug("REST request to get AgileTeam : {}", id);
        Optional<AgileTeamDTO> agileTeamDTO = agileTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agileTeamDTO);
    }

    /**
     * DELETE  /agile-teams/:id : delete the "id" agileTeam.
     *
     * @param id the id of the agileTeamDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agile-teams/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgileTeam(@PathVariable Long id) {
        log.debug("REST request to delete AgileTeam : {}", id);
        agileTeamService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
