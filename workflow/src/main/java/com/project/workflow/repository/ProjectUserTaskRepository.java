package com.project.workflow.repository;
import com.project.workflow.models.ProjectUser;
import com.project.workflow.models.ProjectUserTask;
import com.project.workflow.models.ProjectUserTaskId;
import com.project.workflow.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectUserTaskRepository extends JpaRepository<ProjectUserTask, ProjectUserTaskId> {

//    @Query("SELECT pu FROM ProjectUserTask pu WHERE pu.project.projectId = :projectId AND pu.user.assignerUserId NOT IN (2)")
//    List<Task> findActiveTasks(@Param("projectId") Long projectId, @Param("leaderIds") List<Long> leaderIds);

    @Query("SELECT pu FROM ProjectUserTask pu " +
            "WHERE pu.project.projectId = :projectId " +
            "AND pu.assignerUser.userId NOT IN (:leaderIds)")
    List<ProjectUserTask> findActiveTasks(@Param("projectId") Long projectId, @Param("leaderIds") List<Long> leaderIds);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectUserTask pu SET pu.taskRating = :rating WHERE pu.project.projectId = :projectId AND pu.task.taskId = :taskId AND pu.assignedUser.userId = :userId")
    void findByProjectProjectId(@Param("projectId") Long projectId, @Param("taskId") Long taskId, @Param("userId") Long userId, @Param("rating") double rating);

}