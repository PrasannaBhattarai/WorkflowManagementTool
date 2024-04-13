package com.project.workflow.controller;

import com.project.workflow.models.AuthenticationResponse;
import com.project.workflow.models.RegisterBody;
import com.project.workflow.models.dto.EmailRequest;
import com.project.workflow.service.EmailService;
import com.project.workflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/hello")
public class TestController {

    private final UserService userService;

    private final EmailService emailService;

    public TestController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/test")
    public String hi(){
        System.out.println("HII");
        return "hi";
    }

    @PostMapping("/registerUser")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterBody registerBody){
        return ResponseEntity.ok(userService.register(registerBody));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody RegisterBody registerBody) throws Exception {
        return ResponseEntity.ok(userService.authenticate(registerBody));
    }

    @PostMapping("/authenticateAdmin")
    public ResponseEntity<AuthenticationResponse> adminAuthenticate(@RequestBody RegisterBody registerBody) throws Exception {
        return ResponseEntity.ok(userService.adminAuthenticate(registerBody));
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        String to = emailRequest.getTo();
        String subject = emailRequest.getSubject();
        String text = emailRequest.getText();
        emailService.sendEmail(to, subject, text);
        return "Email sent successfully!";
    }

}
