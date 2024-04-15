package com.project.workflow.controller.admin;

import com.project.workflow.models.UnregisteredUser;
import com.project.workflow.service.admin.AdminService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {


    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/test")
    private ResponseEntity<?> test(HttpServletRequest request) {
        try{
            adminService.authorizeAdmin(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (BadCredentialsException exception){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("/text")
    private ResponseEntity<String> adminGreeting(HttpServletRequest request) {
        try {
            adminService.authorizeAdmin(request);
            return new ResponseEntity<>("Hello, Admin!", HttpStatus.OK);
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/getUsers")
    private ResponseEntity<List<UnregisteredUser>> adminData(HttpServletRequest request) {
        try {
            adminService.authorizeAdmin(request);
            List<UnregisteredUser> users = adminService.findAllRegisteredUsers();
            if (users == null) {
                return null;
            }else{
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/userRegister/{id}")
    public void getUserRegistered(@PathVariable Long id, HttpServletRequest request){
        try{
            adminService.authorizeAdmin(request);
            adminService.registerUser(id);
        }
        catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error registering the new user by admin");
        }
    }

}
