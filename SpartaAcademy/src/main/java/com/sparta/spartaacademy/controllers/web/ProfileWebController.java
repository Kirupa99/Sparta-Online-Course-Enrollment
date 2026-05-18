package com.sparta.spartaacademy.controllers.web;

import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.dtos.TraineeResponseDTO;
import com.sparta.spartaacademy.dtos.TrainerResponseDTO;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import com.sparta.spartaacademy.services.CourseService;
import com.sparta.spartaacademy.services.TraineeService;
import com.sparta.spartaacademy.services.TrainerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;

@Controller
public class ProfileWebController {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final CourseService courseService;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;

    public ProfileWebController(TrainerService trainerService,
                                TraineeService traineeService,
                                CourseService courseService,
                                TrainerRepository trainerRepository,
                                TraineeRepository traineeRepository) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.courseService = courseService;
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        String email = authentication.getName();

        boolean isTrainer = authentication.getAuthorities().stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_TRAINER"));

        if (isTrainer) {
            trainerRepository.findByEmail(email).ifPresent(trainer -> {
                TrainerResponseDTO dto = trainerService.getTrainerById(trainer.getTrainerId());
                List<CourseResponseDTO> courses = courseService.getTrainersCourses(trainer.getTrainerId());
                model.addAttribute("trainer", dto);
                model.addAttribute("courses", courses);
            });
            return "trainer_profile";
        } else {
            traineeRepository.findByEmail(email).ifPresent(trainee -> {
                TraineeResponseDTO dto = traineeService.getTraineeById(trainee.getTraineeId());
                model.addAttribute("trainee", dto);
            });
            return "trainee_profile";
        }
    }
}
