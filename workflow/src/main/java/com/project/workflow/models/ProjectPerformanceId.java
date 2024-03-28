package com.project.workflow.models;

import java.io.Serializable;
import java.util.Objects;

public class ProjectPerformanceId implements Serializable {
    private Long user;
    private Long project;

    public ProjectPerformanceId(Long user, Long project) {
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
        if (o == null || getClass() != o.getClass()) return false;
        ProjectPerformanceId that = (ProjectPerformanceId) o;
        return Objects.equals(user, that.user) && Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, project);
    }
}
