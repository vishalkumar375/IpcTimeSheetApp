package com.ipc.ts.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ipc.ts.service.TaskTypeService;
import com.ipc.ts.web.rest.errors.BadRequestAlertException;
import com.ipc.ts.web.rest.util.HeaderUtil;
import com.ipc.ts.web.rest.util.PaginationUtil;
import com.ipc.ts.service.dto.TaskTypeDTO;
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
 * REST controller for managing TaskType.
 */
@RestController
@RequestMapping("/api")
public class TaskTypeResource {

    private final Logger log = LoggerFactory.getLogger(TaskTypeResource.class);

    private static final String ENTITY_NAME = "taskType";

    private final TaskTypeService taskTypeService;

    public TaskTypeResource(TaskTypeService taskTypeService) {
        this.taskTypeService = taskTypeService;
    }

    /**
     * POST  /task-types : Create a new taskType.
     *
     * @param taskTypeDTO the taskTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskTypeDTO, or with status 400 (Bad Request) if the taskType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-types")
    @Timed
    public ResponseEntity<TaskTypeDTO> createTaskType(@Valid @RequestBody TaskTypeDTO taskTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TaskType : {}", taskTypeDTO);
        if (taskTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskTypeDTO result = taskTypeService.save(taskTypeDTO);
        return ResponseEntity.created(new URI("/api/task-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-types : Updates an existing taskType.
     *
     * @param taskTypeDTO the taskTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskTypeDTO,
     * or with status 400 (Bad Request) if the taskTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the taskTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-types")
    @Timed
    public ResponseEntity<TaskTypeDTO> updateTaskType(@Valid @RequestBody TaskTypeDTO taskTypeDTO) throws URISyntaxException {
        log.debug("REST request to update TaskType : {}", taskTypeDTO);
        if (taskTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaskTypeDTO result = taskTypeService.save(taskTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-types : get all the taskTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taskTypes in body
     */
    @GetMapping("/task-types")
    @Timed
    public ResponseEntity<List<TaskTypeDTO>> getAllTaskTypes(Pageable pageable) {
        log.debug("REST request to get a page of TaskTypes");
        Page<TaskTypeDTO> page = taskTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /task-types/:id : get the "id" taskType.
     *
     * @param id the id of the taskTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/task-types/{id}")
    @Timed
    public ResponseEntity<TaskTypeDTO> getTaskType(@PathVariable Long id) {
        log.debug("REST request to get TaskType : {}", id);
        Optional<TaskTypeDTO> taskTypeDTO = taskTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskTypeDTO);
    }

    /**
     * DELETE  /task-types/:id : delete the "id" taskType.
     *
     * @param id the id of the taskTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskType(@PathVariable Long id) {
        log.debug("REST request to delete TaskType : {}", id);
        taskTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
