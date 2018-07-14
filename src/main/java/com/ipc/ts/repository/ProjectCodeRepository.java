package com.ipc.ts.repository;

import com.ipc.ts.domain.ProjectCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProjectCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectCodeRepository extends JpaRepository<ProjectCode, Long> {

}
