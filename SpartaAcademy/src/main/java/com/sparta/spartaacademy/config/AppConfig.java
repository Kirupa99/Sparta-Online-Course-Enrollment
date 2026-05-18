package com.sparta.spartaacademy.config;

import com.sparta.spartaacademy.entities.Course;
import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.CourseRepository;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import com.sparta.spartaacademy.services.CustomUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CustomUserDetailsService customUserDetailsService,
                                           PasswordEncoder passwordEncoder) throws Exception {
        http
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/signup/trainer",
                                "/signup/trainee",
                                "/login"
                        ).permitAll()
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/api/trainers/**").hasRole("TRAINER")
                        .requestMatchers("/api/trainees/**").hasAnyRole("TRAINER", "TRAINEE")
                        .requestMatchers("/api/courses/**").hasAnyRole("TRAINER", "TRAINEE")
                        .requestMatchers("/course/**").hasRole("TRAINER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

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

                // save trainers first and get back the persisted versions with IDs
                t1 = trainerRepo.save(t1);
                t2 = trainerRepo.save(t2);

                Course c1 = new Course();
                c1.setCourseName("Java Development");
                c1.setDescription("A deep dive into Java from the ground up. Covers core fundamentals including OOP principles, collections, exception handling, and streams, before progressing to advanced topics such as concurrency, design patterns, Spring Boot, and REST API development.");
                c1.setStartDate(LocalDate.of(2026, 4, 10));
                c1.setEndDate(LocalDate.of(2026, 8, 10));
                c1.setMaxStudents(20);
                c1.setTrainers(List.of(t1, t2));

                Course c3 = new Course();
                c3.setCourseName("Test Automation Engineering");
                c3.setDescription("Covers the full spectrum of software testing with a focus on automation. Students will learn manual testing fundamentals before progressing to automated testing with JUnit, Mockito, and Selenium. Topics include test planning, BDD with Cucumber, API testing with Postman, and integrating tests into CI/CD pipelines.");
                c3.setStartDate(LocalDate.of(2026, 6, 1));
                c3.setEndDate(LocalDate.of(2026, 10, 1));
                c3.setMaxStudents(18);
                c3.setTrainers(List.of(t1));

                Course c2 = new Course();
                c2.setCourseName("DevOps Essentials");
                c2.setDescription("A comprehensive introduction to DevOps practices including CI/CD pipelines, containerisation with Docker, infrastructure as code, and cloud deployment strategies. Students will gain hands-on experience with industry-standard tooling.");
                c2.setStartDate(LocalDate.of(2026, 5, 1));
                c2.setEndDate(LocalDate.of(2026, 9, 1));
                c2.setMaxStudents(15);
                c2.setTrainers(List.of(t2));

                courseRepo.saveAll(List.of(c1, c2, c3));

                Trainee tr1 = new Trainee();
                tr1.setFirstName("Charlie");
                tr1.setLastName("Brown");
                tr1.setEmail("charlie@sparta.com");
                tr1.setPassword(encoder.encode("charliepass"));
                tr1.setRole("TRAINEE");
                tr1.setCity("London");
                tr1.setEnrolledDate(LocalDate.now());
                tr1.setCourse(c2);

                Trainee tr2 = new Trainee();
                tr2.setFirstName("Diana");
                tr2.setLastName("Prince");
                tr2.setEmail("diana@sparta.com");
                tr2.setPassword(encoder.encode("dianapass"));
                tr2.setRole("TRAINEE");
                tr2.setCity("Manchester");
                tr2.setEnrolledDate(LocalDate.now());
                tr2.setCourse(c2);

                traineeRepo.saveAll(List.of(tr1, tr2));
            }
        };
    }
}
