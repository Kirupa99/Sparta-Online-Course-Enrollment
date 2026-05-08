package com.sparta.spartaacademy.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @NotBlank(message = "Course name is required")
    @Column(unique = true, nullable = false)
    private String courseName;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Positive(message = "Max students must be a positive number")
    private Integer maxStudents;

    @ManyToMany
    @JoinTable(
            name = "course_trainer",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers;

    @OneToMany(mappedBy = "course")
    private List<Trainee> trainees;

    public Course() {

    }

    public Course(Integer courseId, String courseName, String description, LocalDate startDate, LocalDate endDate, Integer maxStudents, List<Trainer> trainers) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
        this.trainers = trainers;
    }


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }
}




