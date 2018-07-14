package com.ipc.ts.service.mapper;

import com.ipc.ts.domain.*;
import com.ipc.ts.service.dto.TaskTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TaskType and its DTO TaskTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskTypeMapper extends EntityMapper<TaskTypeDTO, TaskType> {



    default TaskType fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaskType taskType = new TaskType();
        taskType.setId(id);
        return taskType;
    }
}
