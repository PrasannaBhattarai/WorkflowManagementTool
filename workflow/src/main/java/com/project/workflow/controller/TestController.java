package com.project.workflow.controller;

import com.project.workflow.models.AuthenticationResponse;
import com.project.workflow.models.RegisterBody;
import com.project.workflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/hello")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
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
}
