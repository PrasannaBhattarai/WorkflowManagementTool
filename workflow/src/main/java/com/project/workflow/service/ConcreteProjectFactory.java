package com.project.workflow.service;

import com.project.workflow.interfaces.ProjectFactory;
import com.project.workflow.models.GroupProject;
import com.project.workflow.models.SoloProject;
import com.project.workflow.models.dto.ProjectDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class ConcreteProjectFactory implements ProjectFactory {


    @Override
    public void createGroupProject(ProjectDTO projectDTO) {
        GroupProject project = new GroupProject();
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectDescription(projectDTO.getProjectDescription());
        project.setProjectType("group");
        project.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        project.setProjectStatus(projectDTO.getProjectStatus());
    }

    @Override
    public void createSoloProject(ProjectDTO projectDTO) {
        SoloProject project = new SoloProject();
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectDescription(projectDTO.getProjectDescription());
        project.setProjectType("solo");
        project.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        project.setProjectStatus(projectDTO.getProjectStatus());
    }
}