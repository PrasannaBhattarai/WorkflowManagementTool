package com.project.workflow.models;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class ProjectUserId implements Serializable {

    private Long user;
    private Long project;

    // Default constructor
    public ProjectUserId() {}

    // Constructor
    public ProjectUserId(Long user, Long project) {
        this.user = user;
        this.project = project;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectUserId)) return false;
        ProjectUserId that = (ProjectUserId) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getProject(), that.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getProject());
    }
}