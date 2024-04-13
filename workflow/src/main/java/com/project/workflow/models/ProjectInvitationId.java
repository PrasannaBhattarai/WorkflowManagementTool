package com.project.workflow.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

public class ProjectInvitationId implements Serializable {

    private User invitedUser;
    private User inviteSenderUser;
    private Project project;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectInvitationId that = (ProjectInvitationId) o;
        return Objects.equals(invitedUser, that.invitedUser) &&
                Objects.equals(inviteSenderUser, that.inviteSenderUser) &&
                Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invitedUser, inviteSenderUser, project);
    }
}
