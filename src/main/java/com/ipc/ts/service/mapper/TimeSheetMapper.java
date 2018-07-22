package com.ipc.ts.service.mapper;

import com.ipc.ts.domain.*;
import com.ipc.ts.service.dto.TimeSheetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TimeSheet and its DTO TimeSheetDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskTypeMapper.class})
public interface TimeSheetMapper extends EntityMapper<TimeSheetDTO, TimeSheet> {

   
    TimeSheetDTO toDto(TimeSheet timeSheet);

   
    TimeSheet toEntity(TimeSheetDTO timeSheetDTO);

    default TimeSheet fromId(Long id) {
        if (id == null) {
            return null;
        }
        TimeSheet timeSheet = new TimeSheet();
        timeSheet.setId(id);
        return timeSheet;
    }
}
