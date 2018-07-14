package com.ipc.ts.service.impl;

import com.ipc.ts.service.TimeSheetService;
import com.ipc.ts.domain.TimeSheet;
import com.ipc.ts.repository.TimeSheetRepository;
import com.ipc.ts.service.dto.TimeSheetDTO;
import com.ipc.ts.service.mapper.TimeSheetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing TimeSheet.
 */
@Service
@Transactional
public class TimeSheetServiceImpl implements TimeSheetService {

    private final Logger log = LoggerFactory.getLogger(TimeSheetServiceImpl.class);

    private final TimeSheetRepository timeSheetRepository;

    private final TimeSheetMapper timeSheetMapper;

    public TimeSheetServiceImpl(TimeSheetRepository timeSheetRepository, TimeSheetMapper timeSheetMapper) {
        this.timeSheetRepository = timeSheetRepository;
        this.timeSheetMapper = timeSheetMapper;
    }

    /**
     * Save a timeSheet.
     *
     * @param timeSheetDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TimeSheetDTO save(TimeSheetDTO timeSheetDTO) {
        log.debug("Request to save TimeSheet : {}", timeSheetDTO);
        TimeSheet timeSheet = timeSheetMapper.toEntity(timeSheetDTO);
        timeSheet = timeSheetRepository.save(timeSheet);
        return timeSheetMapper.toDto(timeSheet);
    }

    /**
     * Get all the timeSheets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TimeSheetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeSheets");
        return timeSheetRepository.findAll(pageable)
            .map(timeSheetMapper::toDto);
    }


    /**
     * Get one timeSheet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TimeSheetDTO> findOne(Long id) {
        log.debug("Request to get TimeSheet : {}", id);
        return timeSheetRepository.findById(id)
            .map(timeSheetMapper::toDto);
    }

    /**
     * Delete the timeSheet by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeSheet : {}", id);
        timeSheetRepository.deleteById(id);
    }
}
