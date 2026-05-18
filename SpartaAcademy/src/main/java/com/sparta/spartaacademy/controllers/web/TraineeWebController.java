package com.sparta.spartaacademy.controllers.web;


import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.dtos.TraineeResponseDTO;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.services.CourseService;
import com.sparta.spartaacademy.services.TraineeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/trainees")
public class TraineeWebController
{
    private final TraineeService traineeservice;
    private final CourseService courseservice;
    private final TraineeRepository traineeRepository;

    public TraineeWebController(TraineeService traineeservice,
                                CourseService courseservice,
                                TraineeRepository traineeRepository)
    {
        this.traineeservice = traineeservice;
        this.courseservice = courseservice;
        this.traineeRepository = traineeRepository;
    }

    @PreAuthorize("hasRole('TRAINEE')")
    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        traineeRepository.findByEmail(email).ifPresent(t -> {
            TraineeResponseDTO dto = traineeservice.getTraineeById(t.getTraineeId());
            model.addAttribute("trainee", dto);
        });
        return "trainee_profile";
    }

    @GetMapping("/courses")
    public String viewTraineeCourses(Model model,
                                     Authentication authentication) {

        String email = authentication.getName();

        List<CourseResponseDTO> courses = courseservice.getCoursesForTrainee(email);

        model.addAttribute("courses", courses);
        model.addAttribute("isTrainer", false);

        return "trainer/view_courses";
    }

    @GetMapping("/courses/{id}")
    public String viewTraineeCourseDetail(@PathVariable Integer id, Model model) {
        CourseResponseDTO course = courseservice.getCourseById(id);

        model.addAttribute("course", course);
        model.addAttribute("isTrainer", false);

        return "trainer/view_course_detail";
    }

}

