package com.project.workflow.repository;

import com.project.workflow.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectRepository extends JpaRepository<Project, Long>  {

    @Modifying
    @Transactional
    @Query("UPDATE Project t SET t.projectType = 'group' WHERE t.projectId = :projectId")
    void changeToGroup(@Param("projectId") Long projectId);
}
