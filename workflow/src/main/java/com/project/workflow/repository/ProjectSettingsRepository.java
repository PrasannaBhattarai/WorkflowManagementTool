package com.project.workflow.repository;

import com.project.workflow.models.ProjectSettings;
import com.project.workflow.models.ProjectSettingsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectSettingsRepository extends JpaRepository<ProjectSettings, ProjectSettingsId> {

    @Query("SELECT ps FROM ProjectSettings ps WHERE ps.project.projectId = :projectId AND ps.settings.projectSettingsId = 1")
    Optional<ProjectSettings> findGuestAnnounce(@Param("projectId") Long projectId);

    @Query("SELECT ps FROM ProjectSettings ps WHERE ps.project.projectId = :projectId AND ps.settings.projectSettingsId = 2")
    Optional<ProjectSettings> findSelAssign(@Param("projectId") Long projectId);
}
