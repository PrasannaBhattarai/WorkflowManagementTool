package com.project.workflow.service.admin;

import com.project.workflow.models.RegisterBody;
import com.project.workflow.models.UnregisteredUser;
import com.project.workflow.models.User;
import com.project.workflow.repository.UnregisteredUserRepository;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UnregisteredUserRepository unregisteredUserRepository;
    private final UserRepository userRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "";
    }

    public void authorizeAdmin(HttpServletRequest request) throws BadCredentialsException {
    Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("custom_cookie".equals(cookie.getName()) && adminEmail.equals(getEmailFromSecurityContext())){
                    return;
                }
            }
        }
        throw  new BadCredentialsException(null);
    }


    public List<UnregisteredUser> findAllRegisteredUsers(){
        return unregisteredUserRepository.findAll();
    }

    public void registerUser(Long id){
        Optional<UnregisteredUser> registerBody = unregisteredUserRepository.findById(id);
        if (registerBody.isPresent()) {
            User user = new User();
            user.setEmail(registerBody.get().getEmail());
            user.setUserName(registerBody.get().getUserName());
            user.setUserRatings(registerBody.get().getUserRatings());
            user.setPassword(registerBody.get().getPassword());
            user.setFirstName(registerBody.get().getFirstName());
            user.setLastName(registerBody.get().getLastName());
            userRepository.save(user);

            //remove from unregistered database
            unregisteredUserRepository.deleteById(id);
        }
    }

    public void declineRequest(Long id){
        unregisteredUserRepository.deleteById(id);
    }

}
