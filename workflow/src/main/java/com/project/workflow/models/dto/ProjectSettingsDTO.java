package com.project.workflow.models.dto;

import java.util.List;

public class ProjectSettingsDTO {

    private boolean groupProject;
    private boolean allowGuestAnnouncements;
    private boolean allowSelfAssignment;
    private List<ProjectUserDTO> emails;

    public boolean isGroupProject() {
        return groupProject;
    }

    public void setGroupProject(boolean groupProject) {
        this.groupProject = groupProject;
    }

    public boolean isAllowGuestAnnouncements() {
        return allowGuestAnnouncements;
    }

    public void setAllowGuestAnnouncements(boolean allowGuestAnnouncements) {
        this.allowGuestAnnouncements = allowGuestAnnouncements;
    }

    public boolean isAllowSelfAssignment() {
        return allowSelfAssignment;
    }

    public void setAllowSelfAssignment(boolean allowSelfAssignment) {
        this.allowSelfAssignment = allowSelfAssignment;
    }

    public List<ProjectUserDTO> getEmails() {
        return emails;
    }

    public void setEmails(List<ProjectUserDTO> emails) {
        this.emails = emails;
    }

    public ProjectSettingsDTO(boolean groupProject, boolean allowGuestAnnouncements, boolean allowSelfAssignment, List<ProjectUserDTO> emails) {
        this.groupProject = groupProject;
        this.allowGuestAnnouncements = allowGuestAnnouncements;
        this.allowSelfAssignment = allowSelfAssignment;
        this.emails = emails;
    }
}
