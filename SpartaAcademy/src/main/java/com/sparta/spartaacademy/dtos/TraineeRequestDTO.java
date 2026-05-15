package com.sparta.spartaacademy.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Schema(description = "Request DTO for creating or updating a trainee")
public class TraineeRequestDTO {

    @NotBlank(message = "First name is required")
    @Schema(description = "First name of the trainee", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name of the trainee", example = "Doe")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email address of the trainee", example = "john.doe@email.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password for the trainee account", example = "securepass123")
    private String password;

    @NotBlank(message = "Role is required")
    @Schema(description = "Role assigned to the trainee", example = "TRAINEE")
    private String role;

    @Schema(description = "City where the trainee is based", example = "London")
    private String city;

    @Schema(description = "Date the trainee enrolled", example = "2024-01-15")
    private LocalDate enrolledDate;

    public TraineeRequestDTO() {}

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public LocalDate getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(LocalDate enrolledDate) { this.enrolledDate = enrolledDate; }
}