package com.project.workflow.models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "ProjectInvitation")
@IdClass(ProjectInvitationId.class) // Specify the composite primary key class
public class ProjectInvitation {

    @Id
    @ManyToOne
    @JoinColumn(name = "invitedUserId")
    private User invitedUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "inviteSenderUserId")
    private User inviteSenderUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @Column(name = "invitationStatus")
    private String invitationStatus;

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public User getInviteSenderUser() {
        return inviteSenderUser;
    }

    public void setInviteSenderUser(User inviteSenderUser) {
        this.inviteSenderUser = inviteSenderUser;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(String invitationStatus) {
        this.invitationStatus = invitationStatus;
    }
}
