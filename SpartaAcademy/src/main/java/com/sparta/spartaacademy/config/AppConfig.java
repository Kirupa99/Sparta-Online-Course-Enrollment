package com.sparta.spartaacademy.config;

import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.CourseRepository;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class AppConfig {

//private final TrainerRepository trainerRepo;

    @Bean
    public CommandLineRunner loadData(TrainerRepository trainerRepo,
                                      TraineeRepository traineeRepo,
                                      CourseRepository courseRepo,
                                      PasswordEncoder encoder) {
        return args -> {
            if (trainerRepo.count() == 0) {

                Trainer t1 = new Trainer();
                t1.setFirstName("Alice");
                t1.setLastName("Smith");
                t1.setEmail("alice@sparta.com");
                t1.setPassword(encoder.encode("alicepass"));
                t1.setRole("TRAINER");
                t1.setPhoneNumber("07700000001");

                Trainer t2 = new Trainer();
                t2.setFirstName("Bob");
                t2.setLastName("Jones");
                t2.setEmail("bob@sparta.com");
                t2.setPassword(encoder.encode("bobpass"));
                t2.setRole("TRAINER");
                t2.setPhoneNumber("07700000002");

                trainerRepo.saveAll(List.of(t1, t2));

                Trainee tr1 = new Trainee();
                tr1.setFirstName("Charlie");
                tr1.setLastName("Brown");
                tr1.setEmail("charlie@sparta.com");
                tr1.setPassword(encoder.encode("charliepass"));
                tr1.setRole("TRAINEE");
                tr1.setCity("London");
                tr1.setEnrolledDate(LocalDate.now());

                Trainee tr2 = new Trainee();
                tr2.setFirstName("Diana");
                tr2.setLastName("Prince");
                tr2.setEmail("diana@sparta.com");
                tr2.setPassword(encoder.encode("dianapass"));
                tr2.setRole("TRAINEE");
                tr2.setCity("Manchester");
                tr2.setEnrolledDate(LocalDate.now());

                traineeRepo.saveAll(List.of(tr1, tr2));
            }
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/trainers/**").hasRole("TRAINER")
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/api/trainees/**").hasAnyRole("TRAINER", "TRAINEE")
                        .requestMatchers("/api/courses/**").hasAnyRole("TRAINER", "TRAINEE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.defaultSuccessUrl("/dashboard", true)
                        .permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
