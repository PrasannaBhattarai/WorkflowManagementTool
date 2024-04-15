package com.project.workflow.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterBodyWithImg {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String userName;

    private int userRatings;

    private MultipartFile imageData;
}