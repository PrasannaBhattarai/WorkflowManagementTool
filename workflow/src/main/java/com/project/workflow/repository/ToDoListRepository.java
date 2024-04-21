package com.project.workflow.repository;

import com.project.workflow.models.ProjectToDoList;
import com.project.workflow.models.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {

    @Query(value = "SELECT t.* " +
            "FROM to_do_list t " +
            "JOIN project_to_do_list pt ON t.to_do_list_id = pt.to_do_list_id " +
            "JOIN Project p ON pt.project_id = p.project_id " +
            "WHERE p.project_id = :projectId AND t.to_do_list_status = 'completed'", nativeQuery = true)
    List<ToDoList> findCompletedToDoListsByProjectId(@Param("projectId") Long projectId);


    @Query(value = "SELECT t.* " +
            "FROM to_do_list t " +
            "JOIN project_to_do_list pt ON t.to_do_list_id = pt.to_do_list_id " +
            "JOIN Project p ON pt.project_id = p.project_id " +
            "WHERE p.project_id = :projectId AND t.to_do_list_status = 'pending'", nativeQuery = true)
    List<ToDoList> findOngoingToDoListsByProjectId(@Param("projectId") Long projectId);

    @Modifying
    @Transactional
    @Query("UPDATE ToDoList t SET t.toDoListStatus = 'completed' WHERE t.toDoListId = :toDoItemId")
    void markToDoListAsCompleted(@Param("toDoItemId") Long toDoItemId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ToDoList t WHERE t.toDoListId = :todoListId")
    void deleteToDoById(Long todoListId);

}