package com.ipc.ts.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TimeSheet entity.
 */
public class TimeSheetDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer empID;

    @NotNull
    private String fullName;

    private String personId;

    private Instant forDate;

    private String forDay;

    private Integer actualHours;

    private String comments;

    private Long departmentId;

    private Long agileTeamId;

    private Long projectCodeId;

    private Long taskTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmpID() {
        return empID;
    }

    public void setEmpID(Integer empID) {
        this.empID = empID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Instant getForDate() {
        return forDate;
    }

    public void setForDate(Instant forDate) {
        this.forDate = forDate;
    }

    public String getForDay() {
        return forDay;
    }

    public void setForDay(String forDay) {
        this.forDay = forDay;
    }

    public Integer getActualHours() {
        return actualHours;
    }

    public void setActualHours(Integer actualHours) {
        this.actualHours = actualHours;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getAgileTeamId() {
        return agileTeamId;
    }

    public void setAgileTeamId(Long agileTeamId) {
        this.agileTeamId = agileTeamId;
    }

    public Long getProjectCodeId() {
        return projectCodeId;
    }

    public void setProjectCodeId(Long projectCodeId) {
        this.projectCodeId = projectCodeId;
    }

    public Long getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(Long taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeSheetDTO timeSheetDTO = (TimeSheetDTO) o;
        if (timeSheetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timeSheetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimeSheetDTO{" +
            "id=" + getId() +
            ", empID=" + getEmpID() +
            ", fullName='" + getFullName() + "'" +
            ", personId='" + getPersonId() + "'" +
            ", forDate='" + getForDate() + "'" +
            ", forDay='" + getForDay() + "'" +
            ", actualHours=" + getActualHours() +
            ", comments='" + getComments() + "'" +
            ", department=" + getDepartmentId() +
            ", agileTeam=" + getAgileTeamId() +
            ", projectCode=" + getProjectCodeId() +
            ", taskType=" + getTaskTypeId() +
            "}";
    }
}
