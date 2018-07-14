package com.ipc.ts.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ProjectCode entity.
 */
public class ProjectCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String projectCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectCodeDTO projectCodeDTO = (ProjectCodeDTO) o;
        if (projectCodeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectCodeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectCodeDTO{" +
            "id=" + getId() +
            ", projectCode='" + getProjectCode() + "'" +
            "}";
    }
}
