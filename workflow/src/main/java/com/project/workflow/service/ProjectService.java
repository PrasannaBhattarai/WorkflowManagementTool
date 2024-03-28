package com.project.workflow.service;

import com.project.workflow.models.Project;
import com.project.workflow.models.dto.ProjectDTO;
import com.project.workflow.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;



}
