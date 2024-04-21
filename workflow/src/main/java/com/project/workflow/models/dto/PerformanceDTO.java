package com.project.workflow.models.dto;

public class PerformanceDTO {

    private String firstName;
    private String lastName;
    private float userRatings;
    private String imageUrl;

    public PerformanceDTO(String firstName, String lastName, float userRatings, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRatings = userRatings;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
