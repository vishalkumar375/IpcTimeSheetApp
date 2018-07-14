package com.ipc.ts.service.mapper;

import com.ipc.ts.domain.*;
import com.ipc.ts.service.dto.AgileTeamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AgileTeam and its DTO AgileTeamDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgileTeamMapper extends EntityMapper<AgileTeamDTO, AgileTeam> {



    default AgileTeam fromId(Long id) {
        if (id == null) {
            return null;
        }
        AgileTeam agileTeam = new AgileTeam();
        agileTeam.setId(id);
        return agileTeam;
    }
}
