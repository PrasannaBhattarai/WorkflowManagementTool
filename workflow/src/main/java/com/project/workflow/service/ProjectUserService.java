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

        ProjectUser projectUser = new ProjectUser(user, project, projectUserDTO.getUserType(), projectUserDTO.getProjectRole());
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

    public List<PerformanceDTO> getPerformanceByProjectId(Long projectId) {
        return projectPerformanceRepository.findTopPerformers(projectId).stream()
                .map(result -> new PerformanceDTO((String) result[0], (String) result[1], (Float) result[2]))
                .collect(Collectors.toList());
    }


}