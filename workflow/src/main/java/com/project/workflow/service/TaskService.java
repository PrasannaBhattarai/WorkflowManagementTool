package com.project.workflow.service;
import com.project.workflow.models.Project;
import com.project.workflow.models.ProjectUserTask;
import com.project.workflow.models.Task;
import com.project.workflow.models.User;
import com.project.workflow.repository.ProjectRepository;
import com.project.workflow.repository.ProjectUserTaskRepository;
import com.project.workflow.repository.TaskRepository;
import com.project.workflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectUserTaskRepository projectUserTaskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectUserTaskRepository projectUserTaskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectUserTaskRepository = projectUserTaskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task createTask(Task task, String assigner, String assignedUser, Long projectId) {

        Task savedTask = new Task();
        savedTask.setTaskStatus("pending");
        savedTask.setTaskDeadline(task.getTaskDeadline());
        savedTask.setTaskDescription(task.getTaskDescription());
        savedTask.setTaskPriority(task.getTaskPriority());
        taskRepository.save(savedTask);

        User assigned = userRepository.findUserByEmail(assignedUser);
        User assignedBy = userRepository.findUserByEmail(assigner);

        Optional<Project> project = projectRepository.findById(projectId);

        if(project.isPresent()){
        ProjectUserTask projectUserTask = new ProjectUserTask();
        projectUserTask.setTask(savedTask);
        projectUserTask.setAssignerUser(assignedBy);
        projectUserTask.setAssignedUser(assigned);
        projectUserTask.setProject(project.get());

        // Save the ProjectUserTask
        projectUserTaskRepository.save(projectUserTask);

        return savedTask;}
        else {
            return null;
        }
    }


    public List<Task> getAllActiveForUserInProject(String userEmail, Long projectId) {
        User user = userRepository.findUserByEmail(userEmail);

        if (user != null) {
            return taskRepository.findActiveTasksForUserInProject(user.getUserId(), projectId);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Task> getDeadlineMissedTasksOrCompletedTasks(String userEmail, Long projectId) {
        User user = userRepository.findUserByEmail(userEmail);

        if (user != null) {
            return taskRepository.findDeadlineMissedTasksOrCompletedTasks(user.getUserId(),projectId);
        } else {
            return Collections.emptyList();
        }

    }

    public void disableTask(Long taskId){
        taskRepository.markTaskAsCompleted(taskId);
    }
}
