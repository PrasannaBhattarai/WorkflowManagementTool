package com.project.workflow.models;

import java.io.Serializable;
import java.util.Objects;

public class ProjectUserTaskId implements Serializable {

    private Long assignerUser;
    private Long assignedUser;
    private Long project;
    private Long task;

    // Default constructor
    public ProjectUserTaskId() {}

    // Constructor
    public ProjectUserTaskId(Long assignerUser, Long assignedUser, Long project, Long task) {
        this.assignerUser = assignerUser;
        this.assignedUser = assignedUser;
        this.project = project;
        this.task = task;
    }

    public Long getAssignerUser() {
        return assignerUser;
    }

    public void setAssignerUser(Long assignerUser) {
        this.assignerUser = assignerUser;
    }

    public Long getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(Long assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public Long getTask() {
        return task;
    }

    public void setTask(Long task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectUserTaskId that = (ProjectUserTaskId) o;
        return Objects.equals(assignerUser, that.assignerUser) &&
                Objects.equals(assignedUser, that.assignedUser) &&
                Objects.equals(project, that.project) &&
                Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignerUser, assignedUser, project, task);
    }
}
