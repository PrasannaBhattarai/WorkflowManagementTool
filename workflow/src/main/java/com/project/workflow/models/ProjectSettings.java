package com.project.workflow.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ProjectSettings")
@IdClass(ProjectSettingsId.class)
public class ProjectSettings {

    @Id
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectSettingsId")
    private Settings settings;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ProjectSettings() {
    }
}