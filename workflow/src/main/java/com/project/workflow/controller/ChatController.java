package com.project.workflow.controller;

import com.project.workflow.models.Chat;
import com.project.workflow.models.ProjectUser;
import com.project.workflow.models.User;
import com.project.workflow.repository.ProjectUserRepository;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.security.JwtService;
import com.project.workflow.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ChatController {

    private final UserRepository userService;
    private final JwtService jwtService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ProjectUserRepository projectUserRepository;
    private final ChatService chatService;


    @Autowired
    public ChatController(UserRepository userService, JwtService jwtService, SimpMessagingTemplate simpMessagingTemplate, ProjectUserRepository projectUserRepository, ChatService chatService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.projectUserRepository = projectUserRepository;
        this.chatService = chatService;
    }

    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : ""; // Get email from authentication
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        String username = jwtService.extractUsername(chatMessage.getToken());
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            Optional<ProjectUser> projectUser = projectUserRepository.findRoleByProjectId(chatMessage.getProjectId(), user.get().getUserId());
            if(projectUser.isPresent()){
                chatMessage.setSender(user.get().getFirstName() + " " + user.get().getLastName() + " (" + projectUser.get().getProjectRole()+")");
                simpMessagingTemplate.convertAndSend("/topic/project-" + chatMessage.getProjectId(), chatMessage);
                chatService.saveChat(chatMessage, user.get(), chatMessage.getProjectId());
            }
        } else {
            throw new RuntimeException("User does not exist in chat!");
        }
    }

    @GetMapping("/topic/project/{projectId}/chat")
    public ResponseEntity<List<ChatMessage>> getProjectChats(@PathVariable Long projectId) {
        try{
            List<ChatMessage> messages = chatService.getAllProjectChats(projectId);
            for (ChatMessage msg: messages) {
                System.out.println(msg.getProjectId()+msg.getSender()+msg.getContent());
            }
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
        catch (Exception exception){
            throw new RuntimeException("Error past chats loading!");
        }
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        if (headerAccessor != null) {
            // Adding username in websocket session
            String username = getUsername();
            headerAccessor.getSessionAttributes().put("username", username);
        } else {
            // Log or handle the case when headerAccessor is null
            System.out.println("HeaderAccessor is null. Unable to add username to session attributes.");
        }
        return chatMessage;
    }

    private String getUsername() {
        String userEmail = getEmailFromSecurityContext();
        User user = userService.findUserByEmail(userEmail);
       return "S";

    }

}