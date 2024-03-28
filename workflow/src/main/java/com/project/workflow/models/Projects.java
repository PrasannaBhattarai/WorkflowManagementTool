package com.project.workflow.models;

import jakarta.persistence.Column;

import java.sql.Timestamp;
import java.util.Objects;

public class Projects {

    private Long projectId;

    private String projectName;

    private String projectDescription;

    private String projectType;

    private Timestamp startDate;

    private Timestamp endDate;

    private String projectStatus;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projects projects = (Projects) o;
        return Objects.equals(projectId, projects.projectId) && Objects.equals(projectName, projects.projectName) && Objects.equals(projectDescription, projects.projectDescription) && Objects.equals(projectType, projects.projectType) && Objects.equals(startDate, projects.startDate) && Objects.equals(endDate, projects.endDate) && Objects.equals(projectStatus, projects.projectStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, projectDescription, projectType, startDate, endDate, projectStatus);
    }
}
