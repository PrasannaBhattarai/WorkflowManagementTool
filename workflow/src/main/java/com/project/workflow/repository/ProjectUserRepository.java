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

    @Query("SELECT pu FROM ProjectUser pu JOIN pu.project p WHERE p.projectStatus = 'ongoing' AND pu.user.userId = :userId")
    List<ProjectUser> findOpenProjects(Long userId);


    @Query("SELECT pu FROM ProjectUser pu WHERE pu.project.projectId = :projectId")
    List<ProjectUser> findByProjectProjectId(@Param("projectId") Long projectId);

    @Query("SELECT pu FROM ProjectUser pu WHERE pu.project.projectId = :projectId and pu.user.userId = :userId")
    Optional<ProjectUser> findRoleByProjectId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query("SELECT pu FROM ProjectUser pu WHERE pu.project.projectId = :projectId AND pu.projectRole='Member'")
    List<ProjectUser> findAllMembers(@Param("projectId") Long projectId);

    @Query("SELECT pu FROM ProjectUser pu WHERE pu.project.projectId = :projectId AND pu.projectRole='Leader'")
    List<ProjectUser> findAllLeaders(@Param("projectId") Long projectId);



}