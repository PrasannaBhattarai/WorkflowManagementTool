package com.project.workflow.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectUsersDTO {

    private Long userId;
    private Long projectId;
    private String userType;
    private String projectRole;

    public ProjectUsersDTO(Long userId, Long projectId, String userType, String projectRole) {
        this.userId = userId;
        this.projectId = projectId;
        this.userType = userType;
        this.projectRole = projectRole;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
}