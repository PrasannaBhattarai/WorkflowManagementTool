package com.project.workflow.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterBody {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String userName;

    private int userRatings;
}
