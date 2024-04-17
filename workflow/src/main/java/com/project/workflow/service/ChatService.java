package com.project.workflow.service;

import com.project.workflow.controller.ChatMessage;
import com.project.workflow.enums.MessageType;
import com.project.workflow.models.*;
import com.project.workflow.repository.ChatRepository;
import com.project.workflow.repository.ProjectChatRepository;
import com.project.workflow.repository.ProjectRepository;
import com.project.workflow.repository.ProjectUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ProjectChatRepository projectChatRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, ProjectChatRepository projectChatRepository, ProjectRepository projectRepository, ProjectUserRepository projectUserRepository) {
        this.chatRepository = chatRepository;
        this.projectChatRepository = projectChatRepository;
        this.projectRepository = projectRepository;
        this.projectUserRepository = projectUserRepository;
    }

    public void saveChat(ChatMessage chat, User user, Long projectId) {
        try {
            Chat chatInstance = new Chat();
            chatInstance.setChatDateTime(LocalDateTime.now());
            chatInstance.setChatMessage(chat.getContent());
            Chat newChat = chatRepository.save(chatInstance);

            ProjectChat projectChat = new ProjectChat();
            projectChat.setChat(newChat);
            projectChat.setSenderUser(user);
            Optional<Project> project = projectRepository.findById(projectId);
            if (project.isPresent()) {
                projectChat.setProject(project.get());
                saveProjectChat(projectChat);
            } else {
                throw new RuntimeException();
            }
        }catch (Exception exception){
            throw new RuntimeException("Failed with websockets");
        }

    }

    public void saveProjectChat(ProjectChat projectChat) {
        projectChatRepository.save(projectChat);
    }


    public List<ChatMessage> getAllProjectChats(Long projectId) {
        // fetching all ProjectChat entities for the given project id
        Optional<List<ProjectChat>> projectChats = projectChatRepository.findAllByProjectIdOrderByChatDateTimeAsc(projectId);

        if (projectChats.isPresent()){
            // merging Chat and ProjectChat entities into a single list
            List<ChatMessage> allChats = new ArrayList<>();
            for (ProjectChat projectChat : projectChats.get()) {
                allChats.add(convertToChatMessage(projectChat));
            }

            // returning the combined list of ChatMessage objects
            return allChats;
        }
        else {
            return null;
        }
    }

    private ChatMessage convertToChatMessage(ProjectChat projectChat) {
        Optional<ProjectUser> projectUser = projectUserRepository.findRoleByProjectId(projectChat.getProject().getProjectId(), projectChat.getSenderUser().getUserId());
        if (projectUser.isPresent()) {

            return ChatMessage.builder()
                    .content(projectChat.getChat().getChatMessage())
                    .sender(projectChat.getSenderUser().getFirstName() + " " + projectChat.getSenderUser().getLastName()+ " ("+projectUser.get().getProjectRole()+ ")")
                    .messageType(MessageType.CHAT)
                    .chatDateTime(projectChat.getChat().getChatDateTime())
                    .projectId(projectChat.getProject().getProjectId())
                    .build();
        }
        else {
            return null;
        }
    }
}