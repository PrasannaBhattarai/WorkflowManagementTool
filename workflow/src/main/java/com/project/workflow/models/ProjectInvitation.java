//package com.project.workflow.models;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "PROJECT_INVITATION")
//public class ProjectInvitation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "invitationId")
//    private Long invitationId;
//
//    @ManyToOne
//    @JoinColumn(name = "invitedUserId")
//    private User invitedUser;
//
//    @ManyToOne
//    @JoinColumn(name = "inviteSenderUserId")
//    private User inviteSenderUser;
//
//    @ManyToOne
//    @JoinColumn(name = "projectId")
//    private Project project;
//
//    @Column(name = "invitationStatus")
//    private String invitationStatus;
//
//    public Long getInvitationId() {
//        return invitationId;
//    }
//
//    public void setInvitationId(Long invitationId) {
//        this.invitationId = invitationId;
//    }
//
//    public User getInvitedUser() {
//        return invitedUser;
//    }
//
//    public void setInvitedUser(User invitedUser) {
//        this.invitedUser = invitedUser;
//    }
//
//    public User getInviteSenderUser() {
//        return inviteSenderUser;
//    }
//
//    public void setInviteSenderUser(User inviteSenderUser) {
//        this.inviteSenderUser = inviteSenderUser;
//    }
//
//    public Project getProject() {
//        return project;
//    }
//
//    public void setProject(Project project) {
//        this.project = project;
//    }
//
//    public String getInvitationStatus() {
//        return invitationStatus;
//    }
//
//    public void setInvitationStatus(String invitationStatus) {
//        this.invitationStatus = invitationStatus;
//    }
//}
//
