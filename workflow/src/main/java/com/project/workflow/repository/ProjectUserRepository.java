package com.project.workflow.repository;

import com.project.workflow.models.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    @Query("SELECT pu FROM ProjectUser pu WHERE pu.user.userId = :userId")
    List<ProjectUser> findByUserUserId(Long userId);

    @Query("SELECT pu FROM ProjectUser pu WHERE pu.project.projectId = :projectId")
    List<ProjectUser> findByProjectProjectId(@Param("projectId") Long projectId);


}