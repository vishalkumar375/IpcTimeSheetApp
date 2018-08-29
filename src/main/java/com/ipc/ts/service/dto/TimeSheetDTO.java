package com.ipc.ts.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.ipc.ts.domain.ProjectCode;
import com.ipc.ts.domain.TaskType;
import com.ipc.ts.domain.User;

/**
 * A DTO for the TimeSheet entity.
 */
public class TimeSheetDTO implements Serializable {

    private Long id;

    private Instant forDate;

    private Integer actualHours;

    private String comments;

    private TaskType taskType;
    
    private ProjectCode projectCode;
    
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getForDate() {
        return forDate;
    }

    public void setForDate(Instant forDate) {
        this.forDate = forDate;
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

    public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

		
	public ProjectCode getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(ProjectCode projectCode) {
		this.projectCode = projectCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
            ", forDate='" + getForDate() + "'" +
            ", actualHours=" + getActualHours() +
            ", comments='" + getComments() + "'" +
            ", taskType=" + getTaskType() +
            ", projectCode=" + getProjectCode() +
            ", user=" + getUser() +
            "}";
    }
}
