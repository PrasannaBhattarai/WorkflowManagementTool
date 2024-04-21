package com.project.workflow.models.dto;

public class UserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String userName;
    private float userRatings;
    private String imageUrl;

    // Default constructor
    public UserDTO() {
    }

    // Constructor
    public UserDTO(String email, String firstName, String lastName, String userName, float userRatings) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userRatings = userRatings;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(float userRatings) {
        this.userRatings = userRatings;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
