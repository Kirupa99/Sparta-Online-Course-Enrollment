package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;

    public CustomUserDetailsService(TrainerRepository trainerRepository, TraineeRepository traineeRepository) {
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Trainer trainer = trainerRepository.findByEmail(email).orElse(null);
        if (trainer != null) {
            return User.builder()
                    .username(trainer.getEmail())
                    .password(trainer.getPassword())
                    .authorities("ROLE_" + trainer.getRole())
                    .build();
        }

        Trainee trainee = traineeRepository.findByEmail(email).orElse(null);
        if (trainee != null) {
            return User.builder()
                    .username(trainee.getEmail())
                    .password(trainee.getPassword())
                    .authorities("ROLE_" + trainee.getRole())
                    .build();
        }

        throw new UsernameNotFoundException("No user found with email: " + email);
    }
}
