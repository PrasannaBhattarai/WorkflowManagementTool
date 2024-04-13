package com.project.workflow.service;

import com.project.workflow.models.*;
import com.project.workflow.models.dto.AnnouncementDTO;
import com.project.workflow.models.response.AnnouncementResponse;
import com.project.workflow.repository.AnnouncementRepository;
import com.project.workflow.repository.ProjectAnnouncementRepository;
import com.project.workflow.repository.ProjectUserRepository;
import com.project.workflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ProjectAnnouncementRepository projectAnnouncementRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;

    private final NotificationService notificationService;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository, ProjectAnnouncementRepository projectAnnouncementRepository, UserRepository userRepository, ProjectUserRepository projectUserRepository, NotificationService notificationService) {
        this.announcementRepository = announcementRepository;
        this.projectAnnouncementRepository = projectAnnouncementRepository;
        this.userRepository = userRepository;
        this.projectUserRepository = projectUserRepository;
        this.notificationService = notificationService;
    }

    public AnnouncementDTO createAnnouncement(AnnouncementDTO announcementDTO, String userEmail, Long projectId){
        try{
            Announcement announcement = new Announcement();
            announcement.setAnnouncementDescription(announcementDTO.getAnnouncement());
            announcement.setAnnouncementDateTime(Timestamp.valueOf((LocalDateTime.now())));
            Announcement createdAnnouncement =announcementRepository.save(announcement);

            User user = userRepository.findUserByEmail(userEmail);

            ProjectAnnouncement projectAnnouncement = new ProjectAnnouncement();
            projectAnnouncement.setAnnouncementId(createdAnnouncement.getAnnouncementId());
            projectAnnouncement.setAnnouncerUserId(user.getUserId());
            projectAnnouncement.setProjectId(projectId);

            projectAnnouncementRepository.save(projectAnnouncement);
            announcementDTO.setCreationDateTime(announcement.getAnnouncementDateTime());
            announcementDTO.setAnnouncementId(createdAnnouncement.getAnnouncementId());
            announcementDTO.setSenderId(userEmail);
            announcementDTO.setProjectId(projectId);

            //send notification to users
            notificationService.createAnnouncementNotification(user,projectId);

            return announcementDTO;

        }
        catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException();
        }
    }

    public List<AnnouncementResponse> getAnnouncementsByProjectId(Long projectId) {
        try {
            List<Announcement> announcements = announcementRepository.findByProjectId(projectId);
            return announcements.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }catch (Exception ex){
            throw new RuntimeException();
        }
    }

    private AnnouncementResponse convertToDTO(Announcement announcement) {
        AnnouncementResponse dto = new AnnouncementResponse();
        dto.setAnnouncementId(announcement.getAnnouncementId());
        dto.setAnnouncement(announcement.getAnnouncementDescription());
        dto.setAnnouncedAt(announcement.getAnnouncementDateTime());
        ProjectAnnouncement projectAnnouncement = projectAnnouncementRepository.findByAnnouncementId(announcement.getAnnouncementId());
        Optional<User> sender = userRepository.findById(projectAnnouncement.getAnnouncerUserId());
        if (sender.isPresent()){
            dto.setSenderName(sender.get().getFirstName()+ " " +sender.get().getLastName());
            Optional<ProjectUser> projectUser = projectUserRepository.findRoleByProjectId(projectAnnouncement.getProjectId(), sender.get().getUserId());
            if (projectUser.isPresent()){
                dto.setSenderRole(projectUser.get().getProjectRole());
            }
        }
        return dto;
    }

    public AnnouncementDTO updateAnnouncement(Long announcementId, AnnouncementDTO announcementDTO) {
        try {
            Announcement announcement = announcementRepository.findById(announcementId)
                    .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

            announcement.setAnnouncementDescription(announcementDTO.getAnnouncement());
            announcement.setAnnouncementDateTime(Timestamp.valueOf(LocalDateTime.now()));
            announcementRepository.save(announcement);

            announcementDTO.setCreationDateTime(announcement.getAnnouncementDateTime());
            return announcementDTO;
        } catch (Exception exception) {
            throw new RuntimeException("Failed to update announcement", exception);
        }
    }

    public void deleteAnnouncement(Long announcementId) {
        try {
            announcementRepository.deleteById(announcementId);

            ProjectAnnouncementId projectAnnouncementId = new ProjectAnnouncementId();
            projectAnnouncementId.setAnnouncementId(announcementId);

            projectAnnouncementRepository.deleteByAnnouncementId(announcementId);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new RuntimeException("Failed to delete announcement", exception);

        }
    }

}
