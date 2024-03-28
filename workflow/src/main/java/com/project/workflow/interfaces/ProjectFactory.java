package com.project.workflow.interfaces;

import com.project.workflow.models.SoloProject;
import com.project.workflow.models.dto.ProjectDTO;

public interface ProjectFactory {
    void createGroupProject(ProjectDTO projectDTO);
    void createSoloProject(ProjectDTO projectDTO);

}
