package com.ipc.ts.service;

import com.ipc.ts.domain.TimeSheet;
import com.ipc.ts.service.dto.TimeSheetDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing TimeSheet.
 */
public interface TimeSheetService {

    /**
     * Save a timeSheet.
     *
     * @param timeSheetDTO the entity to save
     * @return the persisted entity
     */
    TimeSheetDTO save(TimeSheetDTO timeSheetDTO);

    /**
     * Get all the timeSheets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TimeSheetDTO> findAll(Pageable pageable);


    /**
     * Get the "id" timeSheet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TimeSheetDTO> findOne(Long id);

    /**
     * Delete the "id" timeSheet.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    List<TimeSheetDTO> exportTimeSheet(String org, Instant startDate, Instant endDate);
}
