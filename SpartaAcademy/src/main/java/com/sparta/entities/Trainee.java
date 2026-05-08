package com.sparta.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="trainees")
public class Trainee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer traineeId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String cohort;

    private LocalDate enrolledDate;

    @ManyToOne
    @JoinColumn(name="trainer_id")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    public Trainee(){

    }

    public Trainee(Trainer trainer, Integer traineeId, String firstName, String lastName, String email, String cohort, LocalDate enrolledDate, Course course) {
        this.trainer = trainer;
        this.traineeId = traineeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cohort = cohort;
        this.enrolledDate = enrolledDate;
        this.course = course;
    }
}
