package com.project.workflow.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAnnouncementId implements Serializable {
    private Long announcementId;
    private Long announcerUserId;
    private Long projectId;

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    public Long getAnnouncerUserId() {
        return announcerUserId;
    }

    public void setAnnouncerUserId(Long announcerUserId) {
        this.announcerUserId = announcerUserId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}