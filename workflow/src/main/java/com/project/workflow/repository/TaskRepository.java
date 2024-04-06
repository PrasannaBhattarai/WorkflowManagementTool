package com.project.workflow.repository;

import com.project.workflow.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    //SELECT t.*
    //FROM Task t
    //JOIN project_user_task p ON t.task_id = p.task_id
    //WHERE p.assigned_user_id = 1
    //AND p.project_id = 1
    //AND t.task_deadline > CURRENT_TIMESTAMP;
    @Query("SELECT t FROM Task t JOIN t.projectUserTasks put WHERE put.assignedUser.userId = :userId " +
            "AND put.project.projectId = :projectId AND t.taskDeadline > CURRENT_TIMESTAMP")
    List<Task> findActiveTasksForUserInProject(@Param("userId") Long userId, @Param("projectId") Long projectId);


    //SELECT t.* FROM Task t JOIN project_user_task pu on t.task_id=pu.task_id
    //WHERE (t.task_status = 'completed' OR t.task_deadline < CURRENT_TIMESTAMP)
    //AND pu.assigned_user_id = 1;
    @Query("SELECT t FROM Task t JOIN t.projectUserTasks pu WHERE (t.taskStatus = 'completed' OR t.taskDeadline < CURRENT_TIMESTAMP) " +
            "AND pu.assignedUser.userId = :userId")
    List<Task> findDeadlineMissedTasksOrCompletedTasks(@Param("userId") Long userId);

}
