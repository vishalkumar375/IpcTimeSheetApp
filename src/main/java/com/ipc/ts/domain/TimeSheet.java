package com.ipc.ts.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A TimeSheet.
 */
@Entity
@Table(name = "time_sheet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TimeSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "emp_id", nullable = false)
    private Integer empID;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "person_id")
    private String personId;

    @Column(name = "for_date")
    private Instant forDate;

    @Column(name = "for_day")
    private String forDay;

    @Column(name = "actual_hours")
    private Integer actualHours;

    @Column(name = "comments")
    private String comments;

    @OneToOne
    @JoinColumn(unique = true)
    private Department department;

    @OneToOne
    @JoinColumn(unique = true)
    private AgileTeam agileTeam;

    @OneToOne
    @JoinColumn(unique = true)
    private ProjectCode projectCode;

    @OneToOne
    @JoinColumn(unique = true)
    private TaskType taskType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmpID() {
        return empID;
    }

    public TimeSheet empID(Integer empID) {
        this.empID = empID;
        return this;
    }

    public void setEmpID(Integer empID) {
        this.empID = empID;
    }

    public String getFullName() {
        return fullName;
    }

    public TimeSheet fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPersonId() {
        return personId;
    }

    public TimeSheet personId(String personId) {
        this.personId = personId;
        return this;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Instant getForDate() {
        return forDate;
    }

    public TimeSheet forDate(Instant forDate) {
        this.forDate = forDate;
        return this;
    }

    public void setForDate(Instant forDate) {
        this.forDate = forDate;
    }

    public String getForDay() {
        return forDay;
    }

    public TimeSheet forDay(String forDay) {
        this.forDay = forDay;
        return this;
    }

    public void setForDay(String forDay) {
        this.forDay = forDay;
    }

    public Integer getActualHours() {
        return actualHours;
    }

    public TimeSheet actualHours(Integer actualHours) {
        this.actualHours = actualHours;
        return this;
    }

    public void setActualHours(Integer actualHours) {
        this.actualHours = actualHours;
    }

    public String getComments() {
        return comments;
    }

    public TimeSheet comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Department getDepartment() {
        return department;
    }

    public TimeSheet department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public AgileTeam getAgileTeam() {
        return agileTeam;
    }

    public TimeSheet agileTeam(AgileTeam agileTeam) {
        this.agileTeam = agileTeam;
        return this;
    }

    public void setAgileTeam(AgileTeam agileTeam) {
        this.agileTeam = agileTeam;
    }

    public ProjectCode getProjectCode() {
        return projectCode;
    }

    public TimeSheet projectCode(ProjectCode projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(ProjectCode projectCode) {
        this.projectCode = projectCode;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public TimeSheet taskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSheet timeSheet = (TimeSheet) o;
        if (timeSheet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timeSheet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimeSheet{" +
            "id=" + getId() +
            ", empID=" + getEmpID() +
            ", fullName='" + getFullName() + "'" +
            ", personId='" + getPersonId() + "'" +
            ", forDate='" + getForDate() + "'" +
            ", forDay='" + getForDay() + "'" +
            ", actualHours=" + getActualHours() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
