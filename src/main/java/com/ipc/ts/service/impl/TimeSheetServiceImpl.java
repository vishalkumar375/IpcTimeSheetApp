package com.ipc.ts.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipc.ts.domain.TimeSheet;
import com.ipc.ts.repository.TimeSheetRepository;
import com.ipc.ts.security.AuthoritiesConstants;
import com.ipc.ts.security.SecurityUtils;
import com.ipc.ts.service.TimeSheetService;
import com.ipc.ts.service.dto.TimeSheetDTO;
import com.ipc.ts.service.mapper.TimeSheetMapper;
import com.ipc.ts.web.rest.errors.InternalServerErrorException;
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
    public TimeSheetDTO save(TimeSheetDTO timeSheetDTO) throws InternalServerErrorException{
    	TimeSheetDTO timeSheetDTOReturn = null;
    	timeSheetDTOReturn =  saveTimeSheet(timeSheetDTO);
    	if(timeSheetDTO.getCopyToDates()!=null && timeSheetDTO.getCopyToDates().length>0){
    		for (Instant forDate : timeSheetDTO.getCopyToDates()) {
				if (!timeSheetDTO.getForDate().equals(forDate)) {
					TimeSheet timeSheet = timeSheetMapper.toEntity(timeSheetDTO);
					timeSheet.setForDate(forDate);
					List<TimeSheet> sheets = timeSheetRepository.findByTaskTypeAndProjectCodeAndForDateAndUser(timeSheet.getTaskType(),
							timeSheet.getProjectCode(), timeSheet.getForDate(), timeSheet.getUser());
					for(TimeSheet ts : sheets){
						timeSheetRepository.delete(ts);
					}
					timeSheet.setId(null);
					timeSheetDTOReturn = timeSheetMapper.toDto(timeSheetRepository.save(timeSheet));
				}
			}
    	}
       return timeSheetDTOReturn;
    }
    private TimeSheetDTO saveTimeSheet(TimeSheetDTO timeSheetDTO){
    	log.debug("Request to save TimeSheet : {}", timeSheetDTO);
    	if((timeSheetDTO.getId() != null) || (!alreadyLogged(timeSheetDTO) && timeSheetDTO.getId() == null)){
    		 TimeSheet timeSheet = timeSheetMapper.toEntity(timeSheetDTO);
    	     timeSheet = timeSheetRepository.save(timeSheet);
    	     return timeSheetMapper.toDto(timeSheet);
    	}else{
    		throw new InternalServerErrorException("Time is already logged for selected [project code,task type and date] kindly edit to make the changes ");
    	}
      
    }

    private boolean alreadyLogged(TimeSheetDTO timeSheetDTO) {
    	TimeSheet sheet = timeSheetMapper.toEntity(timeSheetDTO);
    	List<TimeSheet> sheets = timeSheetRepository.findByTaskTypeAndProjectCodeAndForDateAndUser(sheet.getTaskType(), sheet.getProjectCode(), sheet.getForDate(),sheet.getUser());
		return sheets!=null && sheets.size()>0;
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
    	if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)){
    		log.debug("Request to get all TimeSheets");
            return timeSheetRepository.findByUserIsCurrentUser(pageable)
                .map(timeSheetMapper::toDto);
    	}else{
    		log.debug("Request to get all TimeSheets");
            return timeSheetRepository.findAll(pageable)
                .map(timeSheetMapper::toDto);
    	}
        
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
