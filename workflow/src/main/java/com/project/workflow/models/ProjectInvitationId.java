package com.project.workflow.models;

import java.io.Serializable;
import jakarta.persistence.*;

public class ProjectInvitationId implements Serializable {

    private Long invitedUser;
    private Long inviteSenderUser;
    private Long project;

    public Long getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(Long invitedUser) {
        this.invitedUser = invitedUser;
    }

    public Long getInviteSenderUser() {
        return inviteSenderUser;
    }

    public void setInviteSenderUser(Long inviteSenderUser) {
        this.inviteSenderUser = inviteSenderUser;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public ProjectInvitationId(Long invitedUser, Long inviteSenderUser, Long project) {
        this.invitedUser = invitedUser;
        this.inviteSenderUser = inviteSenderUser;
        this.project = project;
    }
}
