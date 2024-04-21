package com.project.workflow.repository;

import com.project.workflow.models.ProjectPerformance;
import com.project.workflow.models.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectPerformanceRepository extends JpaRepository<ProjectPerformance, Long> {

    @Query("SELECT u.firstName, u.lastName, u.userRatings, u.email " +
            "FROM User u " +
            "JOIN ProjectUser pu ON u.userId = pu.user.userId " +
            "WHERE pu.project.projectId = :projectId " +
            "ORDER BY u.userRatings DESC LIMIT 3")
    List<Object[]> findTopPerformers(@Param("projectId") Long projectId);
}