package com.project.workflow.models;

import com.project.workflow.enums.NotificationStatus;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ProjectNotification")
@IdClass(ProjectNotificationId.class)
public class ProjectNotification implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "notificationId")
    private Notification notification;

    @Id
    @ManyToOne
    @JoinColumn(name = "senderUserId")
    private User senderUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "receiverUserId")
    private User receiverUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @Column(name = "notificationStatus")
    private NotificationStatus notificationStatus;


    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
