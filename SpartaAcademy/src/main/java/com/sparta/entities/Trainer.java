package com.sparta.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
    @Table(name="trainers")
    public class Trainer
    {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer trainerId;

        @Column(nullable = false)
        private String firstName;

        @Column(nullable = false)
        private String lastName;

        @Column(nullable = false)
        private String email;

        private String specialisation;

        private String phoneNumber;

        @OneToMany(mappedBy = "trainer")
        private List<Trainee> trainees;

        @OneToMany(mappedBy = "trainer")
        private List<Course> courses;

        public Trainer(){

        }
        public Trainer(Integer trainerId, String firstName, String lastName,
                       String email, String specialisation, String phoneNumber) {
            this.trainerId = trainerId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.specialisation = specialisation;
            this.phoneNumber = phoneNumber;
        }

        public Integer getTrainerId() {
            return trainerId;
        }

        public String getFullName(){
            return firstName+" "+lastName;
        }
        }


