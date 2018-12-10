package com.ipc.ts.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ipc.ts.service.TimeSheetService;
import com.ipc.ts.service.dto.TimeSheetDTO;
import com.ipc.ts.service.dto.TimeSheetExportRequestDTO;
import com.ipc.ts.web.rest.errors.BadRequestAlertException;
import com.ipc.ts.web.rest.errors.InternalServerErrorException;
import com.ipc.ts.web.rest.util.HeaderUtil;
import com.ipc.ts.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing TimeSheet.
 */
@RestController
@RequestMapping("/api")
public class TimeSheetResource {

	private final Logger log = LoggerFactory.getLogger(TimeSheetResource.class);

	private static final String ENTITY_NAME = "timeSheet";

	private final TimeSheetService timeSheetService;

	public TimeSheetResource(TimeSheetService timeSheetService) {
		this.timeSheetService = timeSheetService;
	}

	/**
	 * POST /time-sheets : Create a new timeSheet.
	 *
	 * @param timeSheetDTO
	 *            the timeSheetDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new timeSheetDTO, or with status 400 (Bad Request) if the
	 *         timeSheet has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/time-sheets")
	@Timed
	public ResponseEntity<TimeSheetDTO> createTimeSheet(@Valid @RequestBody TimeSheetDTO timeSheetDTO)
			throws URISyntaxException {
		log.debug("REST request to save TimeSheet : {}", timeSheetDTO);
		if (timeSheetDTO.getId() != null) {
			throw new BadRequestAlertException("A new timeSheet cannot already have an ID", ENTITY_NAME, "idexists");
		}
		TimeSheetDTO result = timeSheetService.save(timeSheetDTO);
		return ResponseEntity.created(new URI("/api/time-sheets/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /time-sheets : Updates an existing timeSheet.
	 *
	 * @param timeSheetDTO
	 *            the timeSheetDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         timeSheetDTO, or with status 400 (Bad Request) if the
	 *         timeSheetDTO is not valid, or with status 500 (Internal Server
	 *         Error) if the timeSheetDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/time-sheets")
	@Timed
	public ResponseEntity<TimeSheetDTO> updateTimeSheet(@Valid @RequestBody TimeSheetDTO timeSheetDTO)
			throws URISyntaxException {
		log.debug("REST request to update TimeSheet : {}", timeSheetDTO);
		if (timeSheetDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		try {
			TimeSheetDTO result = timeSheetService.save(timeSheetDTO);
			return ResponseEntity.ok()
					.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeSheetDTO.getId().toString()))
					.body(result);
		} catch (InternalServerErrorException e) {
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "");
		}

	}

	/**
	 * GET /time-sheets : get all the timeSheets.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         timeSheets in body
	 */
	@GetMapping("/time-sheets")
	@Timed
	public ResponseEntity<List<TimeSheetDTO>> getAllTimeSheets(Pageable pageable) {
		log.debug("REST request to get a page of TimeSheets");
		Page<TimeSheetDTO> page = timeSheetService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/time-sheets");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	
	/**
	 * GET /time-sheets-for-period : get all the timeSheets for certain period.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         timeSheets in body
	 */
	@GetMapping("/time-sheets-for-period")
	@Timed
	public ResponseEntity<List<TimeSheetDTO>> getTimeSheetsForPeriod(Pageable pageable,@Valid @RequestBody TimeSheetExportRequestDTO exportRequestDTO) {
		log.debug("REST request to get a page of TimeSheets for a certain period");
		Page<TimeSheetDTO> page = timeSheetService.findAllForPeriod(pageable,exportRequestDTO);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/time-sheets-for-period");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /time-sheets/:id : get the "id" timeSheet.
	 *
	 * @param id
	 *            the id of the timeSheetDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         timeSheetDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/time-sheets/{id}")
	@Timed
	public ResponseEntity<TimeSheetDTO> getTimeSheet(@PathVariable Long id) {
		log.debug("REST request to get TimeSheet : {}", id);
		Optional<TimeSheetDTO> timeSheetDTO = timeSheetService.findOne(id);
		return ResponseUtil.wrapOrNotFound(timeSheetDTO);
	}

	/**
	 * DELETE /time-sheets/:id : delete the "id" timeSheet.
	 *
	 * @param id
	 *            the id of the timeSheetDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/time-sheets/{id}")
	@Timed
	public ResponseEntity<Void> deleteTimeSheet(@PathVariable Long id) {
		log.debug("REST request to delete TimeSheet : {}", id);
		timeSheetService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * DELETE /time-sheets/:id : delete the "id" timeSheet.
	 *
	 * @param id
	 *            the id of the timeSheetDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@PostMapping("/time-sheets/export")
	@Timed
	public  ResponseEntity<List<TimeSheetDTO>> exportTimeSheet(@Valid @RequestBody TimeSheetExportRequestDTO exportRequestDTO) {
		 List<TimeSheetDTO> timesheets = null;
		try {
			timesheets = timeSheetService.exportTimeSheet(exportRequestDTO.getOrg(), exportRequestDTO.getStartDate(),exportRequestDTO.getEndDate(),exportRequestDTO.getUser());
		} catch (Exception ex) {
			log.error("Exception while export : {}", ex);
		}
		return new ResponseEntity<>(timesheets,HttpStatus.OK);
		
	}
}
