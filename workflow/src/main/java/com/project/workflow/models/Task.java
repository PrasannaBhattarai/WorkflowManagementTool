package com.project.workflow.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "taskId")
    private Long taskId;

    @Column(name = "taskDescription")
    private String taskDescription;

    @Column(name = "taskStatus")
    private String taskStatus;

    @Column(name = "taskDeadline")
    private Timestamp taskDeadline;

    @Column(name = "taskPriority")
    private String taskPriority;

    @JsonIgnore
    @OneToMany(mappedBy = "task") // Assuming the field name in ProjectUserTask referencing Task is "task"
    private List<ProjectUserTask> projectUserTasks;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Timestamp getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(Timestamp taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public List<ProjectUserTask> getProjectUserTasks() {
        return projectUserTasks;
    }

    public void setProjectUserTasks(List<ProjectUserTask> projectUserTasks) {
        this.projectUserTasks = projectUserTasks;
    }

}
