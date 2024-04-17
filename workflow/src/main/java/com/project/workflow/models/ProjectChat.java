package com.project.workflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProjectChatId.class)
@Table(name = "ProjectChat")
public class ProjectChat {

    @Id
    @ManyToOne
    @JoinColumn(name = "chatId")
    private Chat chat;

    @Id
    @ManyToOne
    @JoinColumn(name = "senderUserId")
    private User senderUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

}
