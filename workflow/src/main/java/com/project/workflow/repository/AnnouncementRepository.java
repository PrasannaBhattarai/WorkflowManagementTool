package com.project.workflow.repository;

import com.project.workflow.models.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("SELECT a FROM Announcement a JOIN ProjectAnnouncement pa ON a.announcementId = pa.announcementId WHERE pa.projectId = :projectId")
    List<Announcement> findByProjectId(@Param("projectId") Long projectId);
}
