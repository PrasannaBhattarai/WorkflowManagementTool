package com.project.workflow.models.dto;

import java.sql.Timestamp;

public class TasksDTO {
    private Long taskId;
    private String taskDescription;
    private Timestamp taskDeadline;
    private boolean deadlineMissed;
    private String assignedUserEmail;
    private String assignerUserEmail;
    private String taskStatus;
    private String taskPriority;
    private double rating;

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

    public Timestamp getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(Timestamp taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public boolean isDeadlineMissed() {
        return deadlineMissed;
    }

    public void setDeadlineMissed(boolean deadlineMissed) {
        this.deadlineMissed = deadlineMissed;
    }

    public String getAssignedUserEmail() {
        return assignedUserEmail;
    }

    public void setAssignedUserEmail(String assignedUserEmail) {
        this.assignedUserEmail = assignedUserEmail;
    }

    public String getAssignerUserEmail() {
        return assignerUserEmail;
    }

    public void setAssignerUserEmail(String assignerUserEmail) {
        this.assignerUserEmail = assignerUserEmail;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
