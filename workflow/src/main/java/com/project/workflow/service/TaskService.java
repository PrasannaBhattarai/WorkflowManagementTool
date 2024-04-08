package com.project.workflow.service;
import com.project.workflow.models.*;
import com.project.workflow.models.dto.RateTaskDTO;
import com.project.workflow.models.dto.TasksDTO;
import com.project.workflow.models.dto.UserDTO;
import com.project.workflow.models.dto.UserEmails;
import com.project.workflow.models.response.TaskForUsers;
import com.project.workflow.models.response.UserResponse;
import com.project.workflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectUserTaskRepository projectUserTaskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectUserTaskRepository projectUserTaskRepository, UserRepository userRepository, ProjectRepository projectRepository, ProjectUserRepository projectUserRepository) {
        this.taskRepository = taskRepository;
        this.projectUserTaskRepository = projectUserTaskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectUserRepository = projectUserRepository;
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

        // Saving the ProjectUserTask
        projectUserTaskRepository.save(projectUserTask);

        return savedTask;}
        else {
            return null;
        }
    }

    public void assignTasks(String userEmail, TaskForUsers task, Long projectId){
        List<UserEmails> emails = task.getEmails();

        Task savedTask = new Task();
        savedTask.setTaskStatus("pending");
        savedTask.setTaskDeadline(task.getTask().getTaskDeadline());
        savedTask.setTaskDescription(task.getTask().getTaskDescription());
        savedTask.setTaskPriority(task.getTask().getTaskPriority());
        taskRepository.save(savedTask);

        User assignedBy = userRepository.findUserByEmail(userEmail);

        Optional<Project> project = projectRepository.findById(projectId);


        for (UserEmails email:emails) {
            User assigned = userRepository.findUserByEmail(email.getEmail());
            if(project.isPresent()){
                ProjectUserTask projectUserTask = new ProjectUserTask();
                projectUserTask.setTask(savedTask);
                projectUserTask.setAssignerUser(assignedBy);
                projectUserTask.setAssignedUser(assigned);
                projectUserTask.setProject(project.get());
                projectUserTaskRepository.save(projectUserTask);
            }
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

    public List<UserDTO> getUsersForTask(Long projectId){
        List<ProjectUser> users= projectUserRepository.findAllMembers(projectId);
        List<UserDTO> allMembers= new ArrayList<>();
        for (ProjectUser user: users) {
            UserDTO member = new UserDTO();
            member.setEmail(user.getUser().getEmail());
            member.setFirstName(user.getUser().getFirstName());
            member.setLastName(user.getUser().getLastName());
            member.setUserName(user.getProjectRole());
            allMembers.add(member);
        }
        return allMembers;
    }

    //member completed tasks to be rated by leader
    public List<TasksDTO> getAssignedTasks(String userEmail, Long projectId){
        User leader = userRepository.findUserByEmail(userEmail);
        Optional<ProjectUser> projectUser = projectUserRepository.findRoleByProjectId(projectId,leader.getUserId());
        List<ProjectUser> leaderList = projectUserRepository.findAllLeaders(projectId);
        List<Long> leaders = new ArrayList<>();
        for (ProjectUser leaderId: leaderList) {
            leaders.add(leaderId.getUser().getUserId());
        }

        if (projectUser.isPresent() && (projectUser.get().getProjectRole()).equals("Leader")){
            List<ProjectUserTask> userTasks = projectUserTaskRepository.findActiveTasks(projectId,leaders);
            List<TasksDTO> taskList = new ArrayList<>();
            for (ProjectUserTask task: userTasks){
                if(task.getTask().getTaskStatus().equals("completed") && task.getTaskRating()==0){
                TasksDTO tasksDTO = new TasksDTO();
                tasksDTO.setTaskId(task.getTask().getTaskId());
                tasksDTO.setAssignedUserEmail(task.getAssignedUser().getEmail());
                tasksDTO.setAssignerUserEmail(task.getAssignerUser().getEmail());
                tasksDTO.setTaskStatus(task.getTask().getTaskStatus());
                tasksDTO.setTaskPriority(task.getTask().getTaskPriority());
                tasksDTO.setTaskDescription(task.getTask().getTaskDescription());
                tasksDTO.setTaskDeadline(task.getTask().getTaskDeadline());
                tasksDTO.setDeadlineMissed(false);
                taskList.add(tasksDTO);
            }}
            return taskList;
        }
        else {
            return null;
        }
    }

    //pending or deadline missed tasks by members, fetched by leaders
    public List<TasksDTO> getPassiveAssignedTasks(String userEmail, Long projectId){
        User leader = userRepository.findUserByEmail(userEmail);
        Optional<ProjectUser> projectUser = projectUserRepository.findRoleByProjectId(projectId,leader.getUserId());
        List<ProjectUser> leaderList = projectUserRepository.findAllLeaders(projectId);
        List<Long> leaders = new ArrayList<>();
        for (ProjectUser leaderId: leaderList) {
            leaders.add(leaderId.getUser().getUserId());
        }

        if (projectUser.isPresent() && (projectUser.get().getProjectRole()).equals("Leader")){
            List<ProjectUserTask> userTasks = projectUserTaskRepository.findActiveTasks(projectId,leaders);
            List<TasksDTO> taskList = new ArrayList<>();
            for (ProjectUserTask task: userTasks){
                if(task.getTask().getTaskStatus().equals("pending")){
                    TasksDTO tasksDTO = new TasksDTO();
                    tasksDTO.setTaskId(task.getTask().getTaskId());
                    tasksDTO.setAssignedUserEmail(task.getAssignedUser().getEmail());
                    tasksDTO.setAssignerUserEmail(task.getAssignerUser().getEmail());
                    tasksDTO.setTaskStatus(task.getTask().getTaskStatus());
                    tasksDTO.setTaskPriority(task.getTask().getTaskPriority());
                    tasksDTO.setTaskDescription(task.getTask().getTaskDescription());
                    tasksDTO.setTaskDeadline(task.getTask().getTaskDeadline());
                    if (task.getTask().getTaskDeadline().before(new Timestamp(System.currentTimeMillis()))){
                        tasksDTO.setDeadlineMissed(true);
                    }else{
                        tasksDTO.setDeadlineMissed(false);
                    }
                    taskList.add(tasksDTO);
                }}
            return taskList;
        }
        else {
            return null;
        }
    }

    //previously rated tasks
    public List<TasksDTO> getRatedTasks(String userEmail, Long projectId){
        User leader = userRepository.findUserByEmail(userEmail);
        Optional<ProjectUser> projectUser = projectUserRepository.findRoleByProjectId(projectId,leader.getUserId());
        List<ProjectUser> leaderList = projectUserRepository.findAllLeaders(projectId);
        List<Long> leaders = new ArrayList<>();
        for (ProjectUser leaderId: leaderList) {
            leaders.add(leaderId.getUser().getUserId());
        }

        if (projectUser.isPresent() && (projectUser.get().getProjectRole()).equals("Leader")){
            List<ProjectUserTask> userTasks = projectUserTaskRepository.findActiveTasks(projectId,leaders);
            List<TasksDTO> taskList = new ArrayList<>();
            for (ProjectUserTask task: userTasks){
                if(task.getTask().getTaskStatus().equals("completed") && task.getTaskRating()!=0){
                    TasksDTO tasksDTO = new TasksDTO();
                    tasksDTO.setTaskId(task.getTask().getTaskId());
                    tasksDTO.setAssignedUserEmail(task.getAssignedUser().getEmail());
                    tasksDTO.setAssignerUserEmail(task.getAssignerUser().getEmail());
                    tasksDTO.setTaskStatus(task.getTask().getTaskStatus());
                    tasksDTO.setTaskPriority(task.getTask().getTaskPriority());
                    tasksDTO.setTaskDescription(task.getTask().getTaskDescription());
                    tasksDTO.setTaskDeadline(task.getTask().getTaskDeadline());
                    tasksDTO.setDeadlineMissed(false);
                    taskList.add(tasksDTO);
                }}
            return taskList;
        }
        else {
            return null;
        }
    }

    public void rateTask(RateTaskDTO rateTaskDTO, Long projectId){
        User assignedUser = userRepository.findUserByEmail(rateTaskDTO.getEmail());
        if (rateTaskDTO.getRating()>=1 && rateTaskDTO.getRating()<=5){
            projectUserTaskRepository.findByProjectProjectId(projectId,rateTaskDTO.getTaskId(),assignedUser.getUserId(), rateTaskDTO.getRating());
            userRepository.updateRatings((assignedUser.getUserRatings()+rateTaskDTO.getRating())/2, assignedUser.getUserId());
        }
    }
}
