package com.project.workflow.service;


import com.project.workflow.models.*;
import com.project.workflow.repository.ProjectRepository;
import com.project.workflow.repository.ProjectToDoListRepository;
import com.project.workflow.repository.ToDoListRepository;
import com.project.workflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoListService {

    private final ToDoListRepository toDoListRepository;

    private final ProjectToDoListRepository projectToDoListRepository;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    public ToDoListService(ToDoListRepository toDoListRepository, ProjectToDoListRepository projectToDoListRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.toDoListRepository = toDoListRepository;
        this.projectToDoListRepository = projectToDoListRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }


    public ToDoList createToDoList(ToDoList toDoList, Long projectId) {
        ToDoList tempToDo = new ToDoList();
        tempToDo.setToDoListStatus("pending");
        tempToDo.setToDoItemDescription(toDoList.getToDoItemDescription());
        ToDoList toDo =toDoListRepository.save(tempToDo);

        Optional<Project> project = projectRepository.findById(projectId);

        if(project.isPresent()){
            ProjectToDoList projectToDoList = new ProjectToDoList();
            projectToDoList.setToDoListId(toDo.getToDoListId());
            projectToDoList.setProjectId(projectId);
            projectToDoListRepository.save(projectToDoList);

            return toDo;
        } else {
            return null;
        }

    }

    public List<ToDoList> getCompletedToDoListsByProjectId(Long projectId) {
        return toDoListRepository.findCompletedToDoListsByProjectId(projectId);
    }

    public List<ToDoList> getOngoingToDoListsByProjectId(Long projectId) {
        return toDoListRepository.findOngoingToDoListsByProjectId(projectId);
    }

    public void disableToDo(Long toDoItemId){
        toDoListRepository.markToDoListAsCompleted(toDoItemId);
    }

}
