package com.sparta.spartaacademy.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;


@Entity
@Table(name = "trainees")
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer traineeId;

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true, nullable = false)
    private String email;

    private String city;

    private LocalDate enrolledDate;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Trainee(Integer traineeId, String firstName, String lastName, String email, String city, LocalDate enrolledDate, Course course) {
        this.traineeId = traineeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.enrolledDate = enrolledDate;
        this.course = course;
    }

    public Trainee() {

    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
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

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
