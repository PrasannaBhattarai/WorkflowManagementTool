package com.project.workflow.controller;

import com.project.workflow.models.*;
import com.project.workflow.models.dto.*;
import com.project.workflow.models.response.*;
import com.project.workflow.repository.ProjectRepository;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.service.*;
import com.project.workflow.interfaces.ProjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final AnnouncementService announcementService;

    private final ProjectInvitationService projectInvitationService;

    private final NotificationService notificationService;


    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "";
    }

    @Autowired
    public ProjectController(ProjectUserService projectUserService, ProjectRepository projectRepository, ProjectService projectService, ProjectFactory projectFactory, UserRepository userRepository, UserService userService, AnnouncementService announcementService, ProjectInvitationService projectInvitationService, NotificationService notificationService) {
        this.projectUserService = projectUserService;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.userRepository = userRepository;
        this.userService = userService;
        this.announcementService = announcementService;
        this.projectInvitationService = projectInvitationService;
        this.notificationService = notificationService;
    }

    //test purposes only
    @GetMapping("/greeting")
    public String greet() {
        String userEmail = getEmailFromSecurityContext();
        return "Hi " + userEmail;
    }

    @GetMapping("/getNotificationNumber")
    public int getNumberOfNotification(){
        String userEmail = getEmailFromSecurityContext();
        return notificationService.getNumberofNotification(userEmail);
    }

    @PutMapping("/notificationChecked")
    public void notificationChecked(){
        String userEmail = getEmailFromSecurityContext();
        notificationService.completeNotification(userEmail);
    }




    @GetMapping("/getNotifications")
    public List<NotificationResponse> getNotification(){
        try{
            String userEmail = getEmailFromSecurityContext();
            Optional<List<ProjectNotification>> projectNotifications = notificationService.getNotification(userEmail);

            List<NotificationResponse> notificationResponses = new ArrayList<>();
            if (projectNotifications.isPresent()) {
                for (ProjectNotification projectNotification : projectNotifications.get()) {
                    NotificationResponse response = new NotificationResponse();
                    response.setNotificationId(projectNotification.getNotification().getNotificationId());
                    response.setNotificationMessage(projectNotification.getNotification().getNotificationMessage());
                    response.setNotificationType(projectNotification.getNotification().getNotificationType());
                    response.setSenderId(projectNotification.getSenderUser().getEmail());
                    response.setSenderName(projectNotification.getSenderUser().getFirstName() + " " + projectNotification.getSenderUser().getLastName());
                    response.setProjectId(projectNotification.getProject().getProjectId());
                    response.setProjectName(projectNotification.getProject().getProjectName());
                    notificationResponses.add(response);
                }
            }
            return notificationResponses;

        } catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error getting notifications");
        }
    }

    @GetMapping("/getInvitations")
    public List<InvitationResponse> getInvitations(){
        try{
            String userEmail = getEmailFromSecurityContext();
            Optional<List<ProjectInvitation>> projectInvitations = notificationService.getInvitations(userEmail);
            List<InvitationResponse> invitationResponses = new ArrayList<>();
            if (projectInvitations.isPresent()) {
                for (ProjectInvitation projectInvitation : projectInvitations.get()) {
                    InvitationResponse response = new InvitationResponse();
                    response.setSenderName(projectInvitation.getInviteSenderUser().getFirstName() + " " + projectInvitation.getInviteSenderUser().getLastName());
                    response.setSenderId(projectInvitation.getInviteSenderUser().getEmail());
                    response.setProjectId(projectInvitation.getProject().getProjectId());
                    response.setProjectName(projectInvitation.getProject().getProjectName());
                    response.setUserRole(projectInvitation.getUserRole());
                    response.setUserType(projectInvitation.getUserType());
                    invitationResponses.add(response);
                }
            }
            return invitationResponses;
        } catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error getting notifications");
        }
    }




    @PostMapping("/acceptInvitation/{projectId}")
    public void acceptInvitation( @PathVariable Long projectId){
        try{
            String userEmail = getEmailFromSecurityContext();
            projectInvitationService.acceptInvitation(userEmail,projectId);
        }
        catch (Exception ex){
            System.out.println(ex);
            throw new RuntimeException("Error accepting invitation");
        }
    }

    @PostMapping("/rejectInvitation/{projectId}")
    public void rejectInvitation( @PathVariable Long projectId){
        try{
            String userEmail = getEmailFromSecurityContext();
            projectInvitationService.rejectInvitation(userEmail,projectId);
        }
        catch (Exception ex){
            System.out.println(ex);
            throw new RuntimeException("Error accepting invitation");
        }
    }



    @PostMapping("/edit/{projectId}")
    public void editProject(@PathVariable Long projectId, @RequestBody ProjectSettingsDTO projectSettingsDTO){
        String userEmail = getEmailFromSecurityContext();
        projectService.changeSettings(projectId, projectSettingsDTO, userEmail);
    }


    // executed together with /createProjectUser
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        try{
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

            String userEmail = getEmailFromSecurityContext();

            //add projectUser instance
            projectService.createProjectUser(userEmail, projectSaved);

            return new ResponseEntity<>(projectSaved,HttpStatus.CREATED);
        } catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error with creation of Project");
        }
    }


    // executed together with /create
    @PostMapping("/createProjectUser")
    public ResponseEntity<ProjectUserResponse> addProjectUser(@RequestBody ProjectUserDTO projectUserDTO) {
        try{
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
        } catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error with creation of Project");
        }
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

    @GetMapping("/projectUser/{projectId}")
    public ResponseEntity<ProjectUsersDTO> getRoleByProjectUserEmail(@PathVariable Long projectId) {
        String userEmail = getEmailFromSecurityContext();
        ProjectUsersDTO projectUsers = projectUserService.getUserByProjectId(projectId, userEmail);
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

    @GetMapping("/projectType/{projectId}")
    public ResponseEntity<Optional<Project>> getProjectType(@PathVariable Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()){
        return new ResponseEntity<>(project, HttpStatus.OK);
        }
        else{
            return null;
        }
    }

    @GetMapping("/search/{projectId}/{text}")
    public List<UserDTO> searchUsersByEmail(@PathVariable Long projectId,@PathVariable String text) {
        try {
            List<UserDTO> users = userService.getSearchUsers(projectId,text);
            return users.isEmpty() ? null : users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/searchSettings/{projectId}")
    public ProjectSettingsDTO getSettings(@PathVariable Long projectId){
        return projectService.getProjectSettings(projectId);
    }


    @PostMapping("/announcement/{projectId}")
    public ResponseEntity<?> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO, @PathVariable Long projectId) {
        try {
            String userEmail = getEmailFromSecurityContext();
            AnnouncementDTO createdAnnouncement = announcementService.createAnnouncement(announcementDTO, userEmail, projectId);
            return new ResponseEntity<>(createdAnnouncement, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create announcement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAnnouncements/{projectId}")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncementsByProjectId(@PathVariable Long projectId) {
        List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByProjectId(projectId);
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @PutMapping("/edit-announcement/{announcementId}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable Long announcementId, @RequestBody AnnouncementDTO announcementDTO) {
        try {
            AnnouncementDTO updatedAnnouncement = announcementService.updateAnnouncement(announcementId, announcementDTO);
            return new ResponseEntity<>(updatedAnnouncement, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update announcement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-announcement/{announcementId}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long announcementId) {
        try {
            announcementService.deleteAnnouncement(announcementId);
            return new ResponseEntity<>("Announcement deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete announcement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
