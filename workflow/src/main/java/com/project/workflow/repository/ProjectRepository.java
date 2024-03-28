package com.project.workflow.repository;

import com.project.workflow.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>  {
}
