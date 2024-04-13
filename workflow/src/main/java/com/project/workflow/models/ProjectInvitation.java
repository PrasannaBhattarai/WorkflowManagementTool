package com.project.workflow.models;

import com.project.workflow.enums.InvitationStatus;
import com.project.workflow.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "ProjectInvitation")
@IdClass(ProjectInvitationId.class)
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
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "projectId")
    private Project project;

    @Column(name = "invitationStatus")
    private InvitationStatus invitationStatus;

    @Column(name="notificationStatus")
    private NotificationStatus notificationStatus;

    @Column(name = "userType")
    private String userType;

    @Column(name = "userRole")
    private String userRole;

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

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
