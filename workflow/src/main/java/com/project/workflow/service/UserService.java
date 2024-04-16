package com.project.workflow.service;

import com.project.workflow.exceptions.ForbiddenException;
import com.project.workflow.models.*;
import com.project.workflow.models.dto.RegisterBodyWithImg;
import com.project.workflow.models.dto.UserDTO;
import com.project.workflow.models.response.UserResponse;
import com.project.workflow.repository.ProjectUserRepository;
import com.project.workflow.repository.UnregisteredUserRepository;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProjectUserRepository projectUserRepository;
    private final UnregisteredUserRepository unregisteredUserRepository;

    //injecting in-memory user variables
    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;


    public HttpStatus checkRegistrationAvailable(RegisterBodyWithImg registerBody){
        try{
            Optional<User> user = userRepository.findByEmail(registerBody.getEmail());
            if (user.isPresent()) {
                return HttpStatus.BAD_REQUEST;
            }
            else {
                return HttpStatus.OK;
            }
        }catch (Exception exception){
            System.out.println(exception);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }


    public HttpStatus register(RegisterBodyWithImg registerBody) {
        try {

            HttpStatus registrationStatus = checkRegistrationAvailable(registerBody);
            if (registrationStatus != HttpStatus.OK) {
                // registration is not available
                return registrationStatus;
            }

            // Create user object
            var user = UnregisteredUser.builder()
                    .firstName(registerBody.getFirstName())
                    .lastName(registerBody.getLastName())
                    .email(registerBody.getEmail())
                    .password(passwordEncoder.encode(registerBody.getPassword()))
                    .userName(registerBody.getUserName())
                    .userRatings(0)
                    .build();

            // Save user to repository
            //userRepository.save(user);

            unregisteredUserRepository.save(user);

            // Generate JWT token
            //var jwtToken = jwtService.generateToken(user);

            // Save image with encoded email name in resources/static/images directory
            String encodedEmail = encodeString(registerBody.getEmail());
            String imagePath = "static/images/" + encodedEmail + ".jpg";
            saveImage(registerBody.getImageData(), imagePath, encodedEmail);

            return HttpStatus.OK;
//            return AuthenticationResponse.builder()
//                    .token(jwtToken)
//                    .build();
        } catch (Exception exception) {
            System.out.println(exception);
            throw new RuntimeException("Register fail");
        }
    }

    // Method to encode a string using Base64
    public String encodeString(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    // Method to save image data to file
    private void saveImage(MultipartFile imageData, String imagePath, String encodedEmail) throws IOException {
        try {
            byte[] bytes = imageData.getBytes();
            String fileName = encodedEmail + ".jpg";
            File dest = new File("C:\\Users\\Asus\\Documents\\Workflow Management Tool\\WorkflowManagementTool\\workflow\\src\\main\\resources\\static\\images\\" + fileName);
            File frontDest = new File("C:\\Users\\Asus\\Documents\\Workflow Management Tool\\WorkflowManagementTool\\ui\\workflow-react-app\\public\\images\\" + fileName);

            // Write to the frontend destination
            System.out.println("Attempting to read from file in: " + dest.getCanonicalPath());
            FileOutputStream fosDest = new FileOutputStream(dest);
            fosDest.write(bytes);
            fosDest.close();

            // Write to the backend destination
            System.out.println("Attempting to read from file in: " + frontDest.getCanonicalPath());
            FileOutputStream fosFrontDest = new FileOutputStream(frontDest);
            fosFrontDest.write(bytes);
            fosFrontDest.close();

        } catch (Exception exception) {
            System.out.println(exception);
            throw new RuntimeException("Error converting to byte");
        }
    }



    public AuthenticationResponse authenticate(RegisterBody registerBody) throws Exception {
        if (registerBody.getEmail().equals(adminEmail) && registerBody.getPassword().equals(adminPassword)){
            throw new ForbiddenException("Access Forbidden: You are trying to access as an admin.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerBody.getEmail(),
                        registerBody.getPassword()
                )
        );
        var user = userRepository.findByEmail(registerBody.getEmail())
                .orElseThrow(Exception::new);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse adminAuthenticate(RegisterBody registerBody) {
        try {

            // validation of credentials and admin properties
            if (adminEmail.equals(registerBody.getEmail()) && adminPassword.equals(registerBody.getPassword())) {
                // generating JWT token for the admin user
                String jwtToken = jwtService.generateTokenForAdmin();

                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            } else {
                throw new RuntimeException("Invalid admin credentials");
            }
        } catch (Exception e) {
            // Handle authentication failure
            throw new RuntimeException("Admin authentication failed", e);
        }
    }



    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setUserRatings(updatedUser.getUserRatings());
            return userRepository.save(existingUser);
        }
        return null;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<UserDTO> getSearchUsers(Long projectId, String text) {
        List<ProjectUser> projectUsers = projectUserRepository.findByProjectProjectId(projectId);
        List<Long> userIds = projectUsers.stream()
                .map(projectUser -> projectUser.getUser().getUserId())
                .collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            List<User> users = userRepository.findByEmailContaining(text, "admin@gmail.com", userIds);
            if (users != null) {
                return users.stream()
                        .map(user -> new UserDTO(user.getEmail(), user.getFirstName(), user.getLastName(), user.getUserName(), user.getUserRatings()))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }



    public UserResponse getUserByUserEmail(String email) {
        User user=userRepository.findByEmail(email).orElse(null);
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setUserName(user.getUserName());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        return userResponse;
    }
}
