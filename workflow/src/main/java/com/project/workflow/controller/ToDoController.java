package com.project.workflow.controller;

import com.project.workflow.models.ToDoList;
import com.project.workflow.service.ToDoListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/todo")
public class ToDoController {

    private final ToDoListService toDoListService;


    public ToDoController(ToDoListService toDoListService) {
        this.toDoListService = toDoListService;
    }

    @PostMapping("/create")
    public ResponseEntity<ToDoList> createToDo(@RequestBody ToDoList todo, @RequestParam Long projectId) {
        ToDoList createdTask = toDoListService.createToDoList(todo, projectId);
        if (createdTask != null) {
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<ToDoList>> getActiveToDoLists(@RequestParam Long projectId) {
        List<ToDoList> activeToDoLists = toDoListService.getCompletedToDoListsByProjectId(projectId);
        return new ResponseEntity<>(activeToDoLists, HttpStatus.OK);
    }

    @GetMapping("/non-active")
    public ResponseEntity<List<ToDoList>> getNonActiveToDoLists(@RequestParam Long projectId) {
        List<ToDoList> nonActiveToDoLists = toDoListService.getOngoingToDoListsByProjectId(projectId);
        return new ResponseEntity<>(nonActiveToDoLists, HttpStatus.OK);
    }

    @PutMapping("/disable")
    public ResponseEntity<Void> disableActiveToDoItem(@RequestParam Long todoListId) {
        toDoListService.disableToDo(todoListId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
