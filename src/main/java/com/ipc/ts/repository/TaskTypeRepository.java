package com.ipc.ts.repository;

import com.ipc.ts.domain.TaskType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TaskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

}
