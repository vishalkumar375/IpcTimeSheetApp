package com.ipc.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "for_date")
    private Instant forDate;

    @Column(name = "actual_hours")
    private Integer actualHours;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private TaskType taskType;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ProjectCode projectCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public TimeSheet user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
            ", forDate='" + getForDate() + "'" +
            ", actualHours=" + getActualHours() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
