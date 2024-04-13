package com.project.workflow.models.response;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class AnnouncementResponse {

    private Long announcementId;
    private String announcement;
    private String senderName;
    private String senderRole;
    private Timestamp announcedAt;

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public Timestamp getAnnouncedAt() {
        return announcedAt;
    }

    public void setAnnouncedAt(Timestamp announcedAt) {
        this.announcedAt = announcedAt;
    }

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }
}
