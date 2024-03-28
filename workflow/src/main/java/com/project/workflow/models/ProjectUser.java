package com.project.workflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProjectUserId.class)
@Table(name = "ProjectUser")
public class ProjectUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    private Project project;

    @Column(name = "userType")
    private String userType;

    @Column(name = "projectRole")
    private String projectRole;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
}
