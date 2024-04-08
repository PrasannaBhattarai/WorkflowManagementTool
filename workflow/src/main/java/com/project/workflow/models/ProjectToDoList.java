package com.project.workflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProjectToDoListId.class)
@Table(name = "ProjectToDoList")
public class ProjectToDoList {
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

    @Id
    @Column(name = "toDoListId")
    private Long toDoListId;

    @Id
    @Column(name = "projectId")
    private Long projectId;


}
