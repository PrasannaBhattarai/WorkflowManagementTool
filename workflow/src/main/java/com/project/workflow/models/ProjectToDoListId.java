package com.project.workflow.models;

import java.io.Serializable;

public class ProjectToDoListId implements Serializable {

    private Long toDoListId;
    private Long projectId;

    public Long getToDoListId() {
        return toDoListId;
    }

    public void setToDoListId(Long toDoListId) {
        this.toDoListId = toDoListId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
