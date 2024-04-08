package com.project.workflow.repository;

import com.project.workflow.models.ProjectToDoList;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectToDoListRepository extends JpaRepository<ProjectToDoList, Long> {

}
