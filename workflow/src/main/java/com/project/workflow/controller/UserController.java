package com.project.workflow.controller;

import com.project.workflow.models.Project;
import com.project.workflow.models.User;
import com.project.workflow.models.response.UserResponse;
import com.project.workflow.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //test purposes only
    @GetMapping("/test")
    public String test(){
        System.out.println("Hello User!");
        return "User Hello!";
    }

    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : ""; // Get email from authentication
    }


    @GetMapping("/userDetails")
    public UserResponse getUserById() {
        String userEmail = getEmailFromSecurityContext();
        return userService.getUserByUserEmail(userEmail);
    }

    @GetMapping("/getUsername")
    public String getUsername() {
        String userEmail = getEmailFromSecurityContext();
        UserResponse user = userService.getUserByUserEmail(userEmail);
        return user.getFirstName();
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "User with ID: " + userId + " has been deleted successfully.";
    }
}
