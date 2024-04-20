package com.project.workflow.repository;

import com.project.workflow.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t JOIN t.projectUserTasks put WHERE put.assignedUser.userId = :userId AND t.taskStatus = 'pending'" +
            "AND put.project.projectId = :projectId AND t.taskDeadline > CURRENT_TIMESTAMP")
    List<Task> findActiveTasksForUserInProject(@Param("userId") Long userId, @Param("projectId") Long projectId);


    @Query("SELECT t FROM Task t JOIN t.projectUserTasks pu WHERE (t.taskStatus = 'completed' OR t.taskDeadline < CURRENT_TIMESTAMP)" +
            "AND pu.assignedUser.userId = :userId AND pu.project.projectId = :projectId")
    List<Task> findDeadlineMissedTasksOrCompletedTasks(@Param("userId") Long userId, @Param("projectId") Long projectId);


    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.taskStatus = 'completed' WHERE t.taskId = :taskId")
    void markTaskAsCompleted(@Param("taskId") Long taskId);


    @Query("SELECT t FROM Task t JOIN t.projectUserTasks pu WHERE (t.taskStatus = 'completed' OR t.taskDeadline < CURRENT_TIMESTAMP)" +
            "AND pu.project.projectId = :projectId")
    List<Task> getAllDeadlineMissedTasksOrCompletedTasks(@Param("projectId") Long projectId);

    @Query("SELECT t FROM Task t JOIN t.projectUserTasks put WHERE t.taskStatus = 'pending'" +
            "AND put.project.projectId = :projectId AND t.taskDeadline > CURRENT_TIMESTAMP")
    List<Task> getAllActiveForGuestInProject(@Param("projectId") Long projectId);
}
