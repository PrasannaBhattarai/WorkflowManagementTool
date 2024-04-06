package com.project.workflow.controller;

import com.project.workflow.models.Task;
import com.project.workflow.models.User;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "";
    }


    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestParam String assignedUser, @RequestParam Long projectId) {
        String userEmail = getEmailFromSecurityContext();
        Task createdTask = taskService.createTask(task, userEmail, assignedUser, projectId);
        if (createdTask != null) {
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Task>> getAllActiveTasksForUserInProject(@RequestParam Long projectId) {
        String userEmail = getEmailFromSecurityContext();
        List<Task> activeTasks = taskService.getAllActiveForUserInProject(userEmail, projectId);
        return new ResponseEntity<>(activeTasks, HttpStatus.OK);
    }

    @GetMapping("/non-active")
    public ResponseEntity<List<Task>> getDeadlineMissedTasksOrCompletedTasks() {
        String userEmail = getEmailFromSecurityContext();
        List<Task> deadlineMissedOrCompletedTasks = taskService.getDeadlineMissedTasksOrCompletedTasks(userEmail);
        return new ResponseEntity<>(deadlineMissedOrCompletedTasks, HttpStatus.OK);
    }
}
