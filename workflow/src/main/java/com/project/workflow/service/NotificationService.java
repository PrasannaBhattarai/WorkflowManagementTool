package com.project.workflow.service;

import com.project.workflow.enums.NotificationType;
import com.project.workflow.models.*;
import com.project.workflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ProjectNotificationRepository projectNotificationRepository;

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final ProjectInvitationRepository projectInvitationRepository;

    private final ProjectUserRepository projectUserRepository;

    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "";
    }


    public int getNumberofNotification(String email){
        try{
        User user = userRepository.findUserByEmail(email);
        int notifications = projectNotificationRepository.countUnread(user.getUserId());
        int projectInvitations = projectInvitationRepository.countNull(user.getUserId());
        return notifications+projectInvitations;
        }catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException();
        }
    }

    public void completeNotification(String userEmail){
        try{
            User user = userRepository.findUserByEmail(userEmail);
            projectNotificationRepository.checkAllAsRead(user.getUserId());
            projectInvitationRepository.checkAllAsRead(user.getUserId());
        }catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException();
        }
    }

    public Optional<List<ProjectNotification>> getNotification(String userEmail){
        User user = userRepository.findUserByEmail(userEmail);
        return projectNotificationRepository.findByReceiverUserAndProject(user);

    }

    public Notification createNotification(String notificationMessage, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setNotificationMessage(notificationMessage);
        notification.setNotificationType(notificationType.toString());
        return notificationRepository.save(notification);
    }
    public ProjectNotification createProjectNotification(Notification notification, User senderUser, User receiverUser, Project project) {
        ProjectNotification projectNotification = new ProjectNotification();
        projectNotification.setNotification(notification);
        projectNotification.setSenderUser(senderUser);
        projectNotification.setReceiverUser(receiverUser);
        projectNotification.setProject(project);
        return projectNotificationRepository.save(projectNotification);
    }


    public Optional<List<ProjectInvitation>> getInvitations(String userEmail){
        User user = userRepository.findUserByEmail(userEmail);
        Optional<List<ProjectInvitation>> projectInvitation = projectInvitationRepository.findNewInvitations(user.getUserId());
        return projectInvitation;

    }

    public void createAnnouncementNotification(User callingUser, Long projectId){
        try {
            Optional<Project> project = projectRepository.findById(projectId);
            if (project.isPresent()){
                String message = "There is a new announcement in the project "+ project.get().getProjectName();
                Notification newNotification = createNotification(message, NotificationType.announcement);
                List<ProjectUser> projectUsers = projectUserRepository.findByProjectProjectId(projectId);
                for (ProjectUser projectUser: projectUsers) {
                    if (!projectUser.getUser().getUserId().equals(callingUser.getUserId())){
                        createProjectNotification(newNotification, callingUser, projectUser.getUser(),project.get());
                    }
                }
            }
        }catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error with Notification");
        }
    }

    public void createRatedNotification(User assignedUser, Long projectId){
        try {
            String senderEmail = getEmailFromSecurityContext();
            User senderUser = userRepository.findUserByEmail(senderEmail);
            Optional<Project> project = projectRepository.findById(projectId);
            if (project.isPresent()){
                String message = "Your completed task has been rated in project  "+ project.get().getProjectName();
                Notification newNotification = createNotification(message, NotificationType.rated);
                createProjectNotification(newNotification, senderUser, assignedUser,project.get());
            }

        }catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error with Notification");
        }
    }

    public void createTaskAssignNotification(User assignedUser, Project project){
        try {
            String senderEmail = getEmailFromSecurityContext();
            User senderUser = userRepository.findUserByEmail(senderEmail);
            String message = "You have been assigned a new task in project  "+ project.getProjectName();
            Notification newNotification = createNotification(message, NotificationType.task);
            createProjectNotification(newNotification, senderUser, assignedUser,project);
        }catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error with Notification");
        }
    }

}
