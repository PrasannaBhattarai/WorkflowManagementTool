package com.project.workflow.repository;

import com.project.workflow.models.ProjectChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectChatRepository extends JpaRepository<ProjectChat, Long> {

    @Query("SELECT pc FROM ProjectChat pc " +
            "JOIN FETCH pc.chat c " +
            "JOIN FETCH pc.project p " +
            "WHERE p.projectId = :projectId " +
            "ORDER BY c.chatDateTime ASC")
    Optional<List<ProjectChat>> findAllByProjectIdOrderByChatDateTimeAsc(Long projectId);

}
