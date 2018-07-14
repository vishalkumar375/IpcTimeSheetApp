package com.ipc.ts.service.mapper;

import com.ipc.ts.domain.*;
import com.ipc.ts.service.dto.TimeSheetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TimeSheet and its DTO TimeSheetDTO.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, AgileTeamMapper.class, ProjectCodeMapper.class, TaskTypeMapper.class})
public interface TimeSheetMapper extends EntityMapper<TimeSheetDTO, TimeSheet> {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "agileTeam.id", target = "agileTeamId")
    @Mapping(source = "projectCode.id", target = "projectCodeId")
    @Mapping(source = "taskType.id", target = "taskTypeId")
    TimeSheetDTO toDto(TimeSheet timeSheet);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "agileTeamId", target = "agileTeam")
    @Mapping(source = "projectCodeId", target = "projectCode")
    @Mapping(source = "taskTypeId", target = "taskType")
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
