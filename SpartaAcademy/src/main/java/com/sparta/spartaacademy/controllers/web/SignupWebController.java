package com.sparta.spartaacademy.controllers.web;

import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SignupWebController {

    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupWebController(TrainerRepository trainerRepository,
                               TraineeRepository traineeRepository,
                               PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Trainer signup

    @GetMapping("/signup/trainer")
    public String showTrainerSignup() {
        return "signup_trainer";
    }

    @PostMapping("/signup/trainer")
    public String registerTrainer(@RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam(required = false) String phoneNumber,
                                  Model model) {

        if (trainerRepository.existsByEmail(email)) {
            model.addAttribute("error", "An account with that email already exists.");
            return "signup_trainer";
        }

        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setEmail(email);
        trainer.setPassword(passwordEncoder.encode(password));
        trainer.setRole("TRAINER");
        trainer.setPhoneNumber(phoneNumber);
        trainerRepository.save(trainer);

        // log them in automatically after signup
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                trainer, null, List.of(new SimpleGrantedAuthority("ROLE_TRAINER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/dashboard";
    }

    // Trainee signup

    @GetMapping("/signup/trainee")
    public String showTraineeSignup() {
        return "signup_trainee";
    }

    @PostMapping("/signup/trainee")
    public String registerTrainee(@RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam(required = false) String city,
                                  Model model) {

        if (traineeRepository.existsByEmail(email)) {
            model.addAttribute("error", "An account with that email already exists.");
            return "signup_trainee";
        }

        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setEmail(email);
        trainee.setPassword(passwordEncoder.encode(password));
        trainee.setRole("TRAINEE");
        trainee.setCity(city);
        trainee.setEnrolledDate(LocalDate.now());
        traineeRepository.save(trainee);

        // log them in automatically after signup
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                trainee, null, List.of(new SimpleGrantedAuthority("ROLE_TRAINEE"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/dashboard";
    }
}