package com.sparta.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="courses")
    public class Course
    {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer courseId;

        @Column(unique=true, nullable = false)
        private String courseName;

        private Integer durationWeeks;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer maxStudents;

        @ManyToOne
        @JoinColumn(name="trainer_id")
        private Trainer trainer;

        @OneToMany(mappedBy ="course")
        private List<Trainee> trainees;

        public Course(){

        }

        public Course(Integer courseId, String courseName, Integer durationWeeks, LocalDate startDate, LocalDate endDate, Integer maxStudents, Trainer trainer, List<Trainee> trainees) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.durationWeeks = durationWeeks;
            this.startDate = startDate;
            this.endDate = endDate;
            this.maxStudents = maxStudents;
            this.trainer = trainer;
            this.trainees = trainees;
        }
    }

