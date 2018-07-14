package com.ipc.ts.service.mapper;

import com.ipc.ts.domain.*;
import com.ipc.ts.service.dto.ProjectCodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectCode and its DTO ProjectCodeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectCodeMapper extends EntityMapper<ProjectCodeDTO, ProjectCode> {



    default ProjectCode fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectCode projectCode = new ProjectCode();
        projectCode.setId(id);
        return projectCode;
    }
}
