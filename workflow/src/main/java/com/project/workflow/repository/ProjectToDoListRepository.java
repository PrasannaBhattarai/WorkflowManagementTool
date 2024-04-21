package com.project.workflow.repository;

import com.project.workflow.models.ProjectToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ProjectToDoListRepository extends JpaRepository<ProjectToDoList, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ProjectToDoList t WHERE t.toDoListId = :todoListId AND t.projectId = :projectId")
    void deleteToDoById(Long todoListId, Long projectId);

}
