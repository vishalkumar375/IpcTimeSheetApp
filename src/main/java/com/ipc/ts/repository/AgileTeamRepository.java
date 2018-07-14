package com.ipc.ts.repository;

import com.ipc.ts.domain.AgileTeam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AgileTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgileTeamRepository extends JpaRepository<AgileTeam, Long> {

}
