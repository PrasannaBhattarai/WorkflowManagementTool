package com.project.workflow.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//test purposes only
@RequestMapping("/greet")
@RestController
public class GreetingController {
    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : ""; // Get email from authentication
    }

    @GetMapping("/greeting")
    public String greet() {
        String userEmail = getEmailFromSecurityContext();
        return "Hi " + userEmail;
    }

    @GetMapping("/greetMorning")
    public String greetMorning() {
        String userEmail = getEmailFromSecurityContext();
        return "Good morning, " + userEmail;
    }

    @GetMapping("/greetEvening")
    public String greetEvening() {
        String userEmail = getEmailFromSecurityContext();
        return "Good evening, " + userEmail;
    }
}
