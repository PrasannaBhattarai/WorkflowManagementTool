package com.project.workflow.models;

import java.io.Serializable;
import java.util.Objects;

public class ProjectNotificationId implements Serializable {

    private Notification notification;
    private User senderUser;
    private User receiverUser;
    private Project project;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectNotificationId that = (ProjectNotificationId) o;
        return Objects.equals(notification, that.notification) &&
                Objects.equals(senderUser, that.senderUser) &&
                Objects.equals(receiverUser, that.receiverUser) &&
                Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notification, senderUser, receiverUser, project);
    }
}
