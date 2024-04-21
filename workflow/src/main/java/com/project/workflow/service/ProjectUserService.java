package com.project.workflow.service;

import com.project.workflow.models.Project;
import com.project.workflow.models.ProjectPerformance;
import com.project.workflow.models.ProjectUser;
import com.project.workflow.models.User;
import com.project.workflow.models.dto.PerformanceDTO;
import com.project.workflow.models.dto.ProjectUserDTO;
import com.project.workflow.models.dto.ProjectUsersDTO;
import com.project.workflow.repository.ProjectPerformanceRepository;
import com.project.workflow.repository.ProjectRepository;
import com.project.workflow.repository.ProjectUserRepository;
import com.project.workflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectPerformanceRepository projectPerformanceRepository;

    @Autowired
    public ProjectUserService(ProjectUserRepository projectUserRepository, UserRepository userRepository, ProjectRepository projectRepository, ProjectPerformanceRepository projectPerformanceRepository) {
        this.projectUserRepository = projectUserRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectPerformanceRepository = projectPerformanceRepository;
    }

    public ProjectUser addProjectUser(ProjectUserDTO projectUserDTO) {
        User user = userRepository.findByEmail(projectUserDTO.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectUserDTO.getProjectId()).orElseThrow(
                () -> new RuntimeException("Project not found"));

        ProjectUser projectUser = new ProjectUser(user, project, projectUserDTO.getUserType(), "Leader");
        return projectUserRepository.save(projectUser);
    }

    public List<Project> getProjectsByUserEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        System.out.println(userOptional);
        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getUserId();
            List<ProjectUser> projectUsers = projectUserRepository.findByUserUserId(userId);
            return projectUsers.stream()
                    .map(ProjectUser::getProject)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<ProjectUsersDTO> getProjectUsersByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getUserId();
            List<ProjectUser> projectUsers = projectUserRepository.findByUserUserId(userId);
            return projectUsers.stream()
                    .map(projectUser -> new ProjectUsersDTO(projectUser.getUser().getUserId(), projectUser.getProject().getProjectId(), projectUser.getUserType(), projectUser.getProjectRole()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public List<ProjectUsersDTO> getUsersByProjectId(Long projectId) {
        List<ProjectUser> projectUsers = projectUserRepository.findByProjectProjectId(projectId);
        return projectUsers.stream()
                .map(projectUser -> new ProjectUsersDTO(projectUser.getUser().getUserId(), projectId, projectUser.getUserType(), projectUser.getProjectRole()))
                .collect(Collectors.toList());
    }

    public ProjectUsersDTO getUserByProjectId(Long projectId, String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()){
        Optional<ProjectUser> projectUser = projectUserRepository.findRoleByProjectId(projectId,userOptional.get().getUserId());

        if (projectUser.isPresent() && userOptional.isPresent()){
            ProjectUsersDTO projectUsersDTO = new ProjectUsersDTO();
            projectUsersDTO.setProjectRole(projectUser.get().getProjectRole());
            projectUsersDTO.setUserType(projectUser.get().getUserType());
            projectUsersDTO.setProjectId(projectUser.get().getProject().getProjectId());
            return projectUsersDTO;
        } else{
        return null;
        }
        } else {
            return null;
        }
    }

    public List<PerformanceDTO> getPerformanceByProjectId(Long projectId) {
        return projectPerformanceRepository.findTopPerformers(projectId).stream()
                .map(result -> new PerformanceDTO((String) result[0], (String) result[1], (Float) result[2], encodeString((String) result[3])))
                .collect(Collectors.toList());
    }

    private String encodeString(String imageUrl){
        String image = Base64.getEncoder().encodeToString(imageUrl.getBytes());
        return  "images/"+image+".jpg";
    }


}