package com.project.workflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "announcementId")
    private Long announcementId;

    @Column(name = "announcementDescription")
    private String announcementDescription;

    @Column(name = "announcementDateTime")
    private Timestamp announcementDateTime;

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    public String getAnnouncementDescription() {
        return announcementDescription;
    }

    public void setAnnouncementDescription(String announcementDescription) {
        this.announcementDescription = announcementDescription;
    }

    public Timestamp getAnnouncementDateTime() {
        return announcementDateTime;
    }

    public void setAnnouncementDateTime(Timestamp announcementDateTime) {
        this.announcementDateTime = announcementDateTime;
    }
}
