package com.project.workflow.controller.admin;

import com.project.workflow.service.admin.AdminService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
