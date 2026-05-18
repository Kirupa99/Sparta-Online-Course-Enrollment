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

                // --- Trainers ---
                Trainer alice = new Trainer();
                alice.setFirstName("Alice");
                alice.setLastName("Smith");
                alice.setEmail("alice@sparta.com");
                alice.setPassword(encoder.encode("alicepass"));
                alice.setRole("TRAINER");
                alice.setPhoneNumber("07700000001");

                Trainer bob = new Trainer();
                bob.setFirstName("Bob");
                bob.setLastName("Jones");
                bob.setEmail("bob@sparta.com");
                bob.setPassword(encoder.encode("bobpass"));
                bob.setRole("TRAINER");
                bob.setPhoneNumber("07700000002");

                Trainer carol = new Trainer();
                carol.setFirstName("Carol");
                carol.setLastName("White");
                carol.setEmail("carol@sparta.com");
                carol.setPassword(encoder.encode("carolpass"));
                carol.setRole("TRAINER");
                carol.setPhoneNumber("07700000003");

                Trainer david = new Trainer();
                david.setFirstName("David");
                david.setLastName("Brown");
                david.setEmail("david@sparta.com");
                david.setPassword(encoder.encode("davidpass"));
                david.setRole("TRAINER");
                david.setPhoneNumber("07700000004");

                alice = trainerRepo.save(alice);
                bob = trainerRepo.save(bob);
                carol = trainerRepo.save(carol);
                david = trainerRepo.save(david);

                // --- Courses ---
                Course java = new Course();
                java.setCourseName("Java Development");
                java.setDescription("Fundamentals and Advanced Java including Spring Boot");
                java.setStartDate(LocalDate.of(2026, 4, 10));
                java.setEndDate(LocalDate.of(2026, 8, 10));
                java.setMaxStudents(20);
                java.setTrainers(List.of(alice, bob));
                java = courseRepo.save(java);

                Course devops = new Course();
                devops.setCourseName("DevOps Engineering");
                devops.setDescription("CI/CD, Docker, Kubernetes and cloud infrastructure");
                devops.setStartDate(LocalDate.of(2026, 5, 1));
                devops.setEndDate(LocalDate.of(2026, 9, 1));
                devops.setMaxStudents(15);
                devops.setTrainers(List.of(carol));
                devops = courseRepo.save(devops);

                Course dataEng = new Course();
                dataEng.setCourseName("Data Engineering");
                dataEng.setDescription("Python, SQL, Spark and data pipeline development");
                dataEng.setStartDate(LocalDate.of(2026, 6, 1));
                dataEng.setEndDate(LocalDate.of(2026, 10, 1));
                dataEng.setMaxStudents(18);
                dataEng.setTrainers(List.of(david));
                dataEng = courseRepo.save(dataEng);

                Course testing = new Course();
                testing.setCourseName("QA and Testing");
                testing.setDescription("Manual and automated testing with Selenium and JUnit");
                testing.setStartDate(LocalDate.of(2026, 4, 20));
                testing.setEndDate(LocalDate.of(2026, 8, 20));
                testing.setMaxStudents(15);
                testing.setTrainers(List.of(alice, carol));
                testing = courseRepo.save(testing);

                // --- Trainees ---

                // Java course trainees
                Trainee charlie = new Trainee();
                charlie.setFirstName("Charlie");
                charlie.setLastName("Brown");
                charlie.setEmail("charlie@sparta.com");
                charlie.setPassword(encoder.encode("charliepass"));
                charlie.setRole("TRAINEE");
                charlie.setCity("London");
                charlie.setEnrolledDate(LocalDate.of(2026, 4, 10));
                charlie.setCourse(java);

                Trainee diana = new Trainee();
                diana.setFirstName("Diana");
                diana.setLastName("Prince");
                diana.setEmail("diana@sparta.com");
                diana.setPassword(encoder.encode("dianapass"));
                diana.setRole("TRAINEE");
                diana.setCity("Manchester");
                diana.setEnrolledDate(LocalDate.of(2026, 4, 10));
                diana.setCourse(java);

                Trainee ethan = new Trainee();
                ethan.setFirstName("Ethan");
                ethan.setLastName("Hunt");
                ethan.setEmail("ethan@sparta.com");
                ethan.setPassword(encoder.encode("ethanpass"));
                ethan.setRole("TRAINEE");
                ethan.setCity("Birmingham");
                ethan.setEnrolledDate(LocalDate.of(2026, 4, 10));
                ethan.setCourse(java);

                Trainee fiona = new Trainee();
                fiona.setFirstName("Fiona");
                fiona.setLastName("Green");
                fiona.setEmail("fiona@sparta.com");
                fiona.setPassword(encoder.encode("fionapass"));
                fiona.setRole("TRAINEE");
                fiona.setCity("Leeds");
                fiona.setEnrolledDate(LocalDate.of(2026, 4, 10));
                fiona.setCourse(java);

                // DevOps course trainees
                Trainee george = new Trainee();
                george.setFirstName("George");
                george.setLastName("Hall");
                george.setEmail("george@sparta.com");
                george.setPassword(encoder.encode("georgepass"));
                george.setRole("TRAINEE");
                george.setCity("Bristol");
                george.setEnrolledDate(LocalDate.of(2026, 5, 1));
                george.setCourse(devops);

                Trainee hannah = new Trainee();
                hannah.setFirstName("Hannah");
                hannah.setLastName("King");
                hannah.setEmail("hannah@sparta.com");
                hannah.setPassword(encoder.encode("hannahpass"));
                hannah.setRole("TRAINEE");
                hannah.setCity("London");
                hannah.setEnrolledDate(LocalDate.of(2026, 5, 1));
                hannah.setCourse(devops);

                Trainee ian = new Trainee();
                ian.setFirstName("Ian");
                ian.setLastName("Clark");
                ian.setEmail("ian@sparta.com");
                ian.setPassword(encoder.encode("ianpass"));
                ian.setRole("TRAINEE");
                ian.setCity("Edinburgh");
                ian.setEnrolledDate(LocalDate.of(2026, 5, 1));
                ian.setCourse(devops);

                // Data Engineering course trainees
                Trainee julia = new Trainee();
                julia.setFirstName("Julia");
                julia.setLastName("Adams");
                julia.setEmail("julia@sparta.com");
                julia.setPassword(encoder.encode("juliapass"));
                julia.setRole("TRAINEE");
                julia.setCity("Cardiff");
                julia.setEnrolledDate(LocalDate.of(2026, 6, 1));
                julia.setCourse(dataEng);

                Trainee kevin = new Trainee();
                kevin.setFirstName("Kevin");
                kevin.setLastName("Scott");
                kevin.setEmail("kevin@sparta.com");
                kevin.setPassword(encoder.encode("kevinpass"));
                kevin.setRole("TRAINEE");
                kevin.setCity("London");
                kevin.setEnrolledDate(LocalDate.of(2026, 6, 1));
                kevin.setCourse(dataEng);

                // QA course trainees
                Trainee laura = new Trainee();
                laura.setFirstName("Laura");
                laura.setLastName("Evans");
                laura.setEmail("laura@sparta.com");
                laura.setPassword(encoder.encode("laurapass"));
                laura.setRole("TRAINEE");
                laura.setCity("Manchester");
                laura.setEnrolledDate(LocalDate.of(2026, 4, 20));
                laura.setCourse(testing);

                Trainee mike = new Trainee();
                mike.setFirstName("Mike");
                mike.setLastName("Turner");
                mike.setEmail("mike@sparta.com");
                mike.setPassword(encoder.encode("mikepass"));
                mike.setRole("TRAINEE");
                mike.setCity("Sheffield");
                mike.setEnrolledDate(LocalDate.of(2026, 4, 20));
                mike.setCourse(testing);

                Trainee nina = new Trainee();
                nina.setFirstName("Nina");
                nina.setLastName("Patel");
                nina.setEmail("nina@sparta.com");
                nina.setPassword(encoder.encode("ninapass"));
                nina.setRole("TRAINEE");
                nina.setCity("Leicester");
                nina.setEnrolledDate(LocalDate.of(2026, 4, 20));
                nina.setCourse(testing);

                // Unenrolled trainees
                Trainee oliver = new Trainee();
                oliver.setFirstName("Oliver");
                oliver.setLastName("Ward");
                oliver.setEmail("oliver@sparta.com");
                oliver.setPassword(encoder.encode("oliverpass"));
                oliver.setRole("TRAINEE");
                oliver.setCity("London");
                oliver.setEnrolledDate(LocalDate.now());
                oliver.setCourse(null);

                Trainee priya = new Trainee();
                priya.setFirstName("Priya");
                priya.setLastName("Sharma");
                priya.setEmail("priya@sparta.com");
                priya.setPassword(encoder.encode("priyapass"));
                priya.setRole("TRAINEE");
                priya.setCity("Nottingham");
                priya.setEnrolledDate(LocalDate.now());
                priya.setCourse(null);

                traineeRepo.saveAll(List.of(
                        charlie, diana, ethan, fiona,
                        george, hannah, ian,
                        julia, kevin,
                        laura, mike, nina,
                        oliver, priya
                ));
            }
        };
    }
}
