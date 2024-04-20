package com.project.workflow.controller;

import com.project.workflow.models.User;
import com.project.workflow.models.dto.UserRequest;
import com.project.workflow.models.response.UserResponse;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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


    @PostMapping("/editUserDetails")
    public void editUserDetails(@RequestBody UserRequest userRequest) {
        try{
            String userEmail = getEmailFromSecurityContext();
            User user = userRepository.findUserByEmail(userEmail);
            if (userRequest.getFirstName() != null && !userRequest.getFirstName().isEmpty()) {
                user.setFirstName(userRequest.getFirstName());
            }
            if (userRequest.getLastName() != null && !userRequest.getLastName().isEmpty()) {
                user.setLastName(userRequest.getLastName());
            }
            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
                user.setPassword(encodedPassword);
            }
            userRepository.save(user);

         } catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("Error with creation of Project");
    }
    }

    @PostMapping("/editUser")
    public ResponseEntity<UserResponse> editUser(@RequestParam("image") MultipartFile imageFile) {
        String userEmail = getEmailFromSecurityContext();
        UserResponse user = userService.getUserByUserEmail(userEmail);

        String encodedEmail = encodeString(user.getEmail());
        String fileName = encodedEmail + ".jpg";

        // Storing the image in resources/images directory
        String backendDestPath = "C:\\Users\\Asus\\Documents\\Workflow Management Tool\\WorkflowManagementTool\\workflow\\src\\main\\resources\\static\\images\\";
        String frontendDestPath = "C:\\Users\\Asus\\Documents\\Workflow Management Tool\\WorkflowManagementTool\\ui\\workflow-react-app\\public\\images\\";

        try {
            saveImage(imageFile, fileName, backendDestPath);
            saveImage(imageFile, fileName, frontendDestPath);

            user.setImageUrl("images/" + fileName); // Setting image URL in UserResponse
            return ResponseEntity.ok(user);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private void saveImage(MultipartFile imageData, String fileName, String destPath) throws IOException {
        try {
            byte[] bytes = imageData.getBytes();
            File dest = new File(destPath + fileName);

            // If the file already exists, delete it
            if (dest.exists()) {
                dest.delete();
            }

            FileOutputStream fosDest = new FileOutputStream(dest);
            fosDest.write(bytes);
            fosDest.close();
        } catch (Exception exception) {
            System.out.println(exception);
            throw new RuntimeException("Error converting to byte");
        }
    }



    private String encodeString(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }




    @GetMapping("/userDetails")
    public UserResponse getUserById() {
        String userEmail = getEmailFromSecurityContext();
        String encodedEmail = Base64.getEncoder().encodeToString(userEmail.getBytes());
        String image = "images/"+encodedEmail+".jpg";
        UserResponse userResponse =  userService.getUserByUserEmail(userEmail);
        userResponse.setImageUrl(image);
        return userResponse;
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
