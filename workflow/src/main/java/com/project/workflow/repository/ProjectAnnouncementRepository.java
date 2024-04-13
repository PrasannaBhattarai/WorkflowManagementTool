package com.project.workflow.repository;

import com.project.workflow.models.ProjectAnnouncement;
import com.project.workflow.models.ProjectAnnouncementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectAnnouncementRepository extends JpaRepository<ProjectAnnouncement, ProjectAnnouncementId> {

    @Query("SELECT pa FROM ProjectAnnouncement pa WHERE pa.announcementId = :announcementId")
    ProjectAnnouncement findByAnnouncementId(@Param("announcementId") Long announcementId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProjectAnnouncement pa WHERE pa.announcementId = :announcementId")
    void deleteByAnnouncementId(@Param("announcementId") Long announcementId);


}