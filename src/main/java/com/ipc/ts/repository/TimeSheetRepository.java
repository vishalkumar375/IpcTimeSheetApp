package com.ipc.ts.repository;

import com.ipc.ts.domain.TimeSheet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the TimeSheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {

    @Query("select time_sheet from TimeSheet time_sheet where time_sheet.user.login = ?#{principal.username}")
    Page<TimeSheet> findByUserIsCurrentUser(Pageable pageable);

}
