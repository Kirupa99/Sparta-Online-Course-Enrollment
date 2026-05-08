package com.sparta.spartaacademy.dtos;

import java.util.List;

public class TrainerResponseDTO {

    private Integer trainerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<Integer> courseIds;

    public TrainerResponseDTO() {}

    public TrainerResponseDTO(Integer trainerId, String firstName, String lastName,
                              String email, String phoneNumber, List<Integer> courseIds) {
        this.trainerId = trainerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.courseIds = courseIds;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Integer> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Integer> courseIds) {
        this.courseIds = courseIds;
    }
}
