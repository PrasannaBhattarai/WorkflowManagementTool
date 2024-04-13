package com.project.workflow.repository;

import com.project.workflow.models.Project;
import com.project.workflow.models.ProjectNotification;
import com.project.workflow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectNotificationRepository extends JpaRepository<ProjectNotification, Long> {

    @Query("SELECT pn FROM ProjectNotification pn WHERE pn.receiverUser = :receiverUser")
    Optional<List<ProjectNotification>> findByReceiverUserAndProject(@Param("receiverUser") User receiverUser);

    @Query("SELECT COUNT(pn) FROM ProjectNotification pn JOIN pn.notification p WHERE pn.receiverUser.userId = :userId AND pn.notificationStatus IS NULL")
    int countUnread(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectNotification pn SET pn.notificationStatus = 0 WHERE pn.receiverUser.userId = :userId")
    void checkAllAsRead(@Param("userId") Long userId);
}
