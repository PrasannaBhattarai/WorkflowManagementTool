package com.project.workflow.service;

import com.project.workflow.enums.InvitationStatus;
import com.project.workflow.enums.NotificationStatus;
import com.project.workflow.models.Project;
import com.project.workflow.models.ProjectInvitation;
import com.project.workflow.models.ProjectUser;
import com.project.workflow.models.User;
import com.project.workflow.repository.ProjectInvitationRepository;
import com.project.workflow.repository.ProjectRepository;
import com.project.workflow.repository.ProjectUserRepository;
import com.project.workflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectInvitationService {

    private final ProjectInvitationRepository projectInvitationRepository;

    private final ProjectUserRepository projectUserRepository;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectInvitationService(ProjectInvitationRepository projectInvitationRepository, ProjectUserRepository projectUserRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.projectInvitationRepository = projectInvitationRepository;
        this.projectUserRepository = projectUserRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectInvitation sendInvitation(User invitedUser, User inviteSenderUser, Project project, NotificationStatus notificationStatus, String userRole, String userType) {
        ProjectInvitation invitation = new ProjectInvitation();
        invitation.setInvitedUser(invitedUser);
        invitation.setInviteSenderUser(inviteSenderUser);
        invitation.setProject(project);
        invitation.setInvitationStatus(null);
        invitation.setNotificationStatus(notificationStatus);
        invitation.setUserRole(userRole);
        invitation.setUserType(userType);
        System.out.println(invitedUser.getUserId()+" "+inviteSenderUser.getUserId()+" "+ project.getProjectId()+" ");
        return projectInvitationRepository.save(invitation);
    }

    public void acceptInvitation(String userEmail, Long projectId){
        try{
            User user = userRepository.findUserByEmail(userEmail);
            Optional<Project> project = projectRepository.findById(projectId);
            Project tempProject = new Project();
            if(project.isPresent()){
                tempProject.setProjectId(project.get().getProjectId());
                tempProject.setProjectDescription(project.get().getProjectDescription());
                tempProject.setProjectName(project.get().getProjectName());
                tempProject.setProjectStatus(project.get().getProjectStatus());
                tempProject.setProjectType(project.get().getProjectType());
                tempProject.setStartDate(project.get().getStartDate());
                tempProject.setEndDate(project.get().getEndDate());

                Optional<ProjectInvitation> projectInvitation = projectInvitationRepository.findProjectInvitationByProjectIdAndUserId(projectId, user.getUserId());
                if (projectInvitation.isPresent()){
                    ProjectUser projectUser = new ProjectUser(user, tempProject, projectInvitation.get().getUserType(), projectInvitation.get().getUserRole());
                    projectUserRepository.save(projectUser);
                    projectInvitation.get().setInvitationStatus(InvitationStatus.accepted);

                    ProjectInvitation acceptedStatus = new ProjectInvitation();
                    acceptedStatus.setProject(projectInvitation.get().getProject());
                    acceptedStatus.setInvitedUser(projectInvitation.get().getInvitedUser());
                    acceptedStatus.setInvitationStatus(InvitationStatus.accepted);
                    acceptedStatus.setInviteSenderUser(projectInvitation.get().getInviteSenderUser());
                    acceptedStatus.setUserRole(projectInvitation.get().getUserRole());
                    acceptedStatus.setUserType(projectInvitation.get().getUserType());
                    acceptedStatus.setInviteSenderUser(projectInvitation.get().getInviteSenderUser());
                    acceptedStatus.setNotificationStatus(NotificationStatus.read);
                    projectInvitationRepository.save(acceptedStatus);

                }
            }
        }
        catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error creating project user");
        }
    }

    public void rejectInvitation(String userEmail, Long projectId){
        try{
            User user = userRepository.findUserByEmail(userEmail);
            Optional<Project> project = projectRepository.findById(projectId);
            Project tempProject = new Project();
            if(project.isPresent()){
                tempProject.setProjectId(project.get().getProjectId());
                tempProject.setProjectDescription(project.get().getProjectDescription());
                tempProject.setProjectName(project.get().getProjectName());
                tempProject.setProjectStatus(project.get().getProjectStatus());
                tempProject.setProjectType(project.get().getProjectType());
                tempProject.setStartDate(project.get().getStartDate());
                tempProject.setEndDate(project.get().getEndDate());

                Optional<ProjectInvitation> projectInvitation = projectInvitationRepository.findProjectInvitationByProjectIdAndUserId(projectId, user.getUserId());
                if (projectInvitation.isPresent()){
                    projectInvitation.get().setInvitationStatus(InvitationStatus.accepted);
                    ProjectInvitation rejectedStatus = new ProjectInvitation();
                    rejectedStatus.setProject(projectInvitation.get().getProject());
                    rejectedStatus.setInvitedUser(projectInvitation.get().getInvitedUser());
                    rejectedStatus.setInvitationStatus(InvitationStatus.rejected);
                    rejectedStatus.setInviteSenderUser(projectInvitation.get().getInviteSenderUser());
                    rejectedStatus.setUserRole(projectInvitation.get().getUserRole());
                    rejectedStatus.setUserType(projectInvitation.get().getUserType());
                    rejectedStatus.setInviteSenderUser(projectInvitation.get().getInviteSenderUser());
                    rejectedStatus.setNotificationStatus(NotificationStatus.read);
                    projectInvitationRepository.save(rejectedStatus);
                }
            }
        }
        catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error creating project user");
        }
    }
}