package com.project.workflow.repository;

import com.project.workflow.models.ProjectInvitation;
import com.project.workflow.models.ProjectInvitationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectInvitationRepository extends JpaRepository<ProjectInvitation, ProjectInvitationId> {
    @Query("SELECT pi FROM ProjectInvitation pi WHERE pi.project.projectId = :projectId AND pi.invitedUser.userId = :userId")
    Optional<ProjectInvitation> findProjectInvitationByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query("SELECT pi FROM ProjectInvitation pi WHERE pi.invitedUser.userId = :userId AND pi.invitationStatus IS NULL")
    Optional<List<ProjectInvitation>> findNewInvitations(@Param("userId") Long userId);

    @Query("SELECT COUNT(pi) FROM ProjectInvitation pi WHERE pi.invitedUser.userId = :userId AND pi.notificationStatus IS NULL")
    int countNull(@Param("userId") Long userId);


    @Modifying
    @Transactional
    @Query("UPDATE ProjectInvitation pi SET pi.notificationStatus = 0 WHERE pi.invitedUser.userId = :userId")
    void checkAllAsRead(@Param("userId") Long userId);

}
