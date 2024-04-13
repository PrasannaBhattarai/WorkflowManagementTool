package com.project.workflow.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProjectAnnouncementId.class)
@Table(name = "ProjectAnnouncement")
public class ProjectAnnouncement {

    @Id
    @Column(name = "announcementId")
    private Long announcementId;

    @Id
    @Column(name = "announcerUserId")
    private Long announcerUserId;

    @Id
    @Column(name = "projectId")
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
