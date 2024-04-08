package com.project.workflow.models;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "Settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "projectSettingsId")
    private Long projectSettingsId;

    @Column(name = "projectSettingsName")
    private String projectSettingsName;

    public Long getProjectSettingsId() {
        return projectSettingsId;
    }

    public void setProjectSettingsId(Long projectSettingsId) {
        this.projectSettingsId = projectSettingsId;
    }

    public String getProjectSettingsName() {
        return projectSettingsName;
    }

    public void setProjectSettingsName(String projectSettingsName) {
        this.projectSettingsName = projectSettingsName;
    }
}
