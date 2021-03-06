package com.ipc.ts.repository;

import java.time.Instant;
import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ipc.ts.domain.ProjectCode;
import com.ipc.ts.domain.TaskType;
import com.ipc.ts.domain.TimeSheet;
import com.ipc.ts.domain.User;
import com.ipc.ts.service.dto.TimeSheetDTO;

/**
 * Spring Data  repository for the TimeSheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {

    @Query("select time_sheet from TimeSheet time_sheet where time_sheet.user.login = ?#{principal.username}")
    Page<TimeSheet> findByUserIsCurrentUser(Pageable pageable);
    
    List<TimeSheet> findByTaskTypeAndProjectCodeAndForDateAndUser(TaskType taskType,ProjectCode projectCode,Instant forDate,User user);
    
    @Query("select time_sheet from TimeSheet time_sheet where time_sheet.user.organization.name = ?1 and time_sheet.forDate between ?2 and ?3 Order by time_sheet.user.empId ASC, time_sheet.forDate ASC")
    List<TimeSheet> findByOrgAndDate(String org,  Instant startDate, Instant endDate);
    
    @Query("select time_sheet from TimeSheet time_sheet where time_sheet.user.login = ?1 and time_sheet.user.organization.name = ?2 and time_sheet.forDate between ?3 and ?4")
    List<TimeSheet> findByOrgLoginAndDate(String login,String org,  Instant startDate, Instant endDate);

    @Query("select time_sheet from TimeSheet time_sheet where time_sheet.user.organization.name = ?2 and time_sheet.forDate between ?3 and ?4")
	Page<TimeSheet> findByUserIsCurrentUserForPeriod(Pageable pageable, String string, Instant instant, Instant instant2);
    
    @Query("select time_sheet from TimeSheet time_sheet where time_sheet.user.organization.name = ?2 and time_sheet.forDate between ?3 and ?4")
	Page<TimeSheet> findAllForPeriod(Pageable pageable, String string, Instant instant, Instant instant2);

}
