package com.sparta.spartaacademy;

import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.sparta")
@EntityScan("com.sparta.spartaacademy.entities")
@EnableJpaRepositories("com.sparta.spartaacademy.repositories")
public class SpartaAcademyApplication {

    public static void main(String[] args)
    {

        ApplicationContext context =  SpringApplication.run(SpartaAcademyApplication.class, args);

        TrainerRepository trainerRepo = context.getBean(TrainerRepository.class);
        System.out.println("=== Trainers ===");
        for (Trainer trainer : trainerRepo.findAll()) {
            System.out.println(trainer.getFirstName() + " " + trainer.getLastName() + " | " + trainer.getEmail() + " | " + trainer.getRole());
        }

        TraineeRepository traineeRepo = context.getBean(TraineeRepository.class);
        System.out.println("=== Trainees ===");
        for (Trainee trainee : traineeRepo.findAll()) {
            System.out.println(trainee.getFirstName() + " " + trainee.getLastName() + " | " + trainee.getEmail() + " | " + trainee.getRole());
        }
    }

}
