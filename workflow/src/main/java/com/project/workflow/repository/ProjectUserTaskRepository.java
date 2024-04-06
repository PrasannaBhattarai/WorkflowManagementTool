package com.project.workflow.repository;
import com.project.workflow.models.ProjectUserTask;
import com.project.workflow.models.ProjectUserTaskId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserTaskRepository extends JpaRepository<ProjectUserTask, ProjectUserTaskId> {

}