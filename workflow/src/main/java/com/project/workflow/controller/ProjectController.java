package com.project.workflow.controller;

import com.project.workflow.models.Project;
import com.project.workflow.models.ProjectUser;
import com.project.workflow.models.User;
import com.project.workflow.models.dto.*;
import com.project.workflow.models.response.ProjectUserResponse;
import com.project.workflow.models.response.UserResponse;
import com.project.workflow.repository.ProjectRepository;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.service.ProjectService;
import com.project.workflow.interfaces.ProjectFactory;
import com.project.workflow.service.ProjectUserService;
import com.project.workflow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectUserService projectUserService;

    private final ProjectRepository projectRepository;

    private final ProjectService projectService;

    private final ProjectFactory projectFactory;

    private final UserRepository userRepository;

    private final UserService userService;


    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : ""; // Get email from authentication
    }

    public ProjectController(ProjectUserService projectUserService, ProjectRepository projectRepository, ProjectService projectService, ProjectFactory projectFactory, UserRepository userRepository, UserService userService) {
        this.projectUserService = projectUserService;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    //test purposes only
    @GetMapping("/greeting")
    public String greet() {
        String userEmail = getEmailFromSecurityContext();
        return "Hi " + userEmail;
    }


    // executed together with /createProjectUser
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectDescription(projectDTO.getProjectDescription());
        project.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        project.setProjectStatus("ongoing");
        project.setProjectType(projectDTO.getProjectType().equalsIgnoreCase("group") ? "group" : "solo");
        Project projectSaved = projectRepository.save(project);
        if (projectDTO.getProjectType().equalsIgnoreCase("group")){
            System.out.println(projectDTO.getUsersId());
            //invitation part
            projectSaved.getProjectId();
        }

        return new ResponseEntity<>(projectSaved,HttpStatus.CREATED);
    }


    // executed together with /create
    @PostMapping("/createProjectUser")
    public ResponseEntity<ProjectUserResponse> addProjectUser(@RequestBody ProjectUserDTO projectUserDTO) {
        ProjectUser createdProjectUser = projectUserService.addProjectUser(projectUserDTO);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(createdProjectUser.getUser().getUserId());
        userResponse.setFirstName(createdProjectUser.getUser().getFirstName());
        userResponse.setLastName(createdProjectUser.getUser().getLastName());
        userResponse.setEmail(createdProjectUser.getUser().getEmail());
        userResponse.setUserName(createdProjectUser.getUser().getUserName());

        ProjectUserResponse projectUserResponse = new ProjectUserResponse();
        projectUserResponse.setUser(userResponse);
        projectUserResponse.setProject(createdProjectUser.getProject());
        projectUserResponse.setUserType(createdProjectUser.getUserType());
        projectUserResponse.setProjectRole(createdProjectUser.getProjectRole());

        return new ResponseEntity<>(projectUserResponse, HttpStatus.CREATED);
    }


    //together with projectusers
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjectsByUserEmail() {
        String userEmail = getEmailFromSecurityContext();
        List<Project> projects = projectUserService.getProjectsByUserEmail(userEmail);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    //together with projects
    @GetMapping("/projectUsers")
    public ResponseEntity<List<ProjectUsersDTO>> getProjectsByProjectUserEmail() {
        String userEmail = getEmailFromSecurityContext();
        List<ProjectUsersDTO> projectUsers = projectUserService.getProjectUsersByEmail(userEmail);
        return new ResponseEntity<>(projectUsers, HttpStatus.OK);
    }

    @GetMapping("/projectUsers/project/{projectId}")
    public ResponseEntity<List<ProjectUsersDTO>> getUsersByProjectId(@PathVariable Long projectId) {
        List<ProjectUsersDTO> projectUsers = projectUserService.getUsersByProjectId(projectId);
        return new ResponseEntity<>(projectUsers, HttpStatus.OK);
    }

    @GetMapping("/performance/{projectId}")
    public ResponseEntity<List<PerformanceDTO>> getTopPerformers(@PathVariable Long projectId) {
        List<PerformanceDTO> performance = projectUserService.getPerformanceByProjectId(projectId);
        return new ResponseEntity<>(performance, HttpStatus.OK);
    }

    @GetMapping("/search/{text}")
    public List<UserDTO> searchUsersByEmail(@PathVariable String text) {
        try {
            List<UserDTO> users = userService.getSearchUsers(text);
            return users.isEmpty() ? null : users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
