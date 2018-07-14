package com.ipc.ts.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TaskType entity.
 */
public class TaskTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String taskType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskTypeDTO taskTypeDTO = (TaskTypeDTO) o;
        if (taskTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskTypeDTO{" +
            "id=" + getId() +
            ", taskType='" + getTaskType() + "'" +
            "}";
    }
}
