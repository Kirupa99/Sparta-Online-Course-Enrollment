package com.sparta.spartaacademy.dtos;

import java.time.LocalDate;

public class TraineeDTO {
    private Integer traineeId;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private LocalDate enrolledDate;
    private Integer courseId;

    public TraineeDTO() {}

    public TraineeDTO(Integer traineeId, String firstName, String lastName,
                      String email, String city, LocalDate enrolledDate,
                      Integer courseId) {
        this.traineeId = traineeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.enrolledDate = enrolledDate;
        this.courseId = courseId;
    }

    public Integer getTraineeId() { return traineeId; }
    public void setTraineeId(Integer traineeId) { this.traineeId = traineeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public LocalDate getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(LocalDate enrolledDate) { this.enrolledDate = enrolledDate; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
}
