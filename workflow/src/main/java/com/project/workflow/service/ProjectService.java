package com.project.workflow.service;

import com.project.workflow.enums.InvitationStatus;
import com.project.workflow.models.*;
import com.project.workflow.models.dto.ProjectDTO;
import com.project.workflow.models.dto.ProjectSettingsDTO;
import com.project.workflow.models.dto.ProjectUserDTO;
import com.project.workflow.models.dto.UserEmails;
import com.project.workflow.repository.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSettingsRepository projectSettingsRepository;
    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ProjectInvitationService projectInvitationService;

    public void changeSettings(Long projectId, ProjectSettingsDTO projectSettingsDTO, String inviteSender){
        Optional<ProjectSettings> guestAnnounce = projectSettingsRepository.findGuestAnnounce(projectId);
        Optional<ProjectSettings> selfAssign = projectSettingsRepository.findSelAssign(projectId);
        Optional<Settings> guestSettings = settingsRepository.findById(1L);
        Optional<Settings> selfSettings = settingsRepository.findById(2L);
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
        }

        //changes project from solo to group
        if (projectSettingsDTO.isGroupProject()){
            projectRepository.changeToGroup(projectId);
        }

        if(guestAnnounce.isPresent() && projectSettingsDTO.isAllowGuestAnnouncements()){
            ProjectSettings updated = new ProjectSettings();
            updated.setSettings(guestAnnounce.get().getSettings());
            updated.setProject(guestAnnounce.get().getProject());
            projectSettingsRepository.save(updated);
        }
        if (selfAssign.isPresent() && projectSettingsDTO.isAllowSelfAssignment()) {
            ProjectSettings updated = new ProjectSettings();
            updated.setSettings(selfAssign.get().getSettings());
            updated.setProject(selfAssign.get().getProject());
            projectSettingsRepository.save(updated);
        }
        if(guestAnnounce.isPresent() && !projectSettingsDTO.isAllowGuestAnnouncements()){
            projectSettingsRepository.delete(guestAnnounce.get());
        }
        if (selfAssign.isPresent() && !projectSettingsDTO.isAllowSelfAssignment()) {
            projectSettingsRepository.delete(selfAssign.get());
        }
        if (selfAssign.isEmpty() && projectSettingsDTO.isAllowSelfAssignment()) {
            ProjectSettings updated = new ProjectSettings();
            updated.setSettings(selfSettings.get());
            updated.setProject(project.get());
            projectSettingsRepository.save(updated);
        }
        if(guestAnnounce.isEmpty() && projectSettingsDTO.isAllowGuestAnnouncements()){
            ProjectSettings updated = new ProjectSettings();
            updated.setSettings(guestSettings.get());
            updated.setProject(project.get());
            projectSettingsRepository.save(updated);
        }

        for (ProjectUserDTO userDTO: projectSettingsDTO.getEmails()) {
            User user = userRepository.findUserByEmail(userDTO.getUserId());
            User sender = userRepository.findUserByEmail(inviteSender);
            projectInvitationService.sendInvitation(user, sender, tempProject, null, userDTO.getUserType(), userDTO.getProjectRole());
//            ProjectUser projectUser = new ProjectUser(user, tempProject, userDTO.getUserType(), userDTO.getProjectRole());
//            projectUserRepository.save(projectUser);
        }
    }

    public ProjectSettingsDTO getProjectSettings(Long projectId){
        Optional<ProjectSettings> guestAnnounce = projectSettingsRepository.findGuestAnnounce(projectId);
        Optional<ProjectSettings> selfAssign = projectSettingsRepository.findSelAssign(projectId);
        Optional<Project> project = projectRepository.findById(projectId);
        ProjectSettingsDTO projectSettingsDTO = new ProjectSettingsDTO();
        if (project.isPresent() && project.get().getProjectType().equals("group")){
            if(guestAnnounce.isPresent()){
                projectSettingsDTO.setAllowGuestAnnouncements(true);
            }else {
                projectSettingsDTO.setAllowGuestAnnouncements(false);
            }
            if(selfAssign.isPresent()){
                projectSettingsDTO.setAllowSelfAssignment(true);
            }else {
                projectSettingsDTO.setAllowSelfAssignment(false);
            }
            projectSettingsDTO.setGroupProject(true);
            return projectSettingsDTO;
        }
        else {
            projectSettingsDTO.setAllowGuestAnnouncements(false);
            projectSettingsDTO.setAllowSelfAssignment(false);
            projectSettingsDTO.setGroupProject(false);
            return projectSettingsDTO;
        }
    }


}
