package com.project.workflow.controller;

import com.project.workflow.models.User;
import com.project.workflow.models.response.UserResponse;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final UserRepository userService;

    public ChatController(UserRepository userService) {
        this.userService = userService;
    }

    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : ""; // Get email from authentication
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessage;
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