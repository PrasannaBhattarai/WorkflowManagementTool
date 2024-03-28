package com.project.workflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProjectUserTaskId.class) // Using IdClass for composite key
@Table(name = "ProjectUserTask")
public class ProjectUserTask {

    @Id
    @ManyToOne
    @JoinColumn(name = "assignerUserId", referencedColumnName = "userId")
    private User assignerUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "assignedUserId", referencedColumnName = "userId")
    private User assignedUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    private Project project;

    @Id
    @ManyToOne
    @JoinColumn(name = "taskId", referencedColumnName = "taskId")
    private Task task;

    @Column(name = "taskRating")
    private int taskRating;

    public User getAssignerUser() {
        return assignerUser;
    }

    public void setAssignerUser(User assignerUser) {
        this.assignerUser = assignerUser;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getTaskRating() {
        return taskRating;
    }

    public void setTaskRating(int taskRating) {
        this.taskRating = taskRating;
    }
}
