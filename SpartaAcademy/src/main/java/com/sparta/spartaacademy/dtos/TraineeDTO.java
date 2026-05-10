package com.sparta.spartaacademy.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Trainee data transfer object")
public class TraineeDTO {

    @Schema(description = "Trainee ID", example = "1")
    private Integer traineeId;

    @Schema(description = "First name of the trainee", example = "John")
    private String firstName;

    @Schema(description = "Last name of the trainee", example = "Doe")
    private String lastName;

    @Schema(description = "Email address of the trainee", example = "john.doe@email.com")
    private String email;

    @Schema(description = "City where the trainee is based", example = "London")
    private String city;

    @Schema(description = "Date the trainee enrolled", example = "2024-01-15")
    private LocalDate enrolledDate;

    @Schema(description = "ID of the course the trainee is enrolled on", example = "2")
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
