package com.project.workflow.models;

import java.io.Serializable;
import jakarta.persistence.*;

public class ProjectSettingsId implements Serializable {

    private Long project;
    private Long settings;

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public Long getSettings() {
        return settings;
    }

    public void setSettings(Long settings) {
        this.settings = settings;
    }

    public ProjectSettingsId(Long project, Long settings) {
        this.project = project;
        this.settings = settings;
    }

    public ProjectSettingsId() {
    }
}
