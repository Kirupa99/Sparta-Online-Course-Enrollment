package com.sparta.spartaacademy.controllers.web;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.services.CourseService;
import com.sparta.spartaacademy.services.TrainerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/course")
public class CourseViewController {

    private final CourseService courseService;
    private final TrainerService trainerService;

    public CourseViewController(CourseService courseService, TrainerService trainerService) {
        this.courseService = courseService;
        this.trainerService = trainerService;
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/create_course")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainer/create_course";
    }


    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/create_course")
    public String createCourse(
            @ModelAttribute CourseRequestDTO dto,
            Model model) {

        try {
            courseService.createCourse(dto);
            return "redirect:/trainer/view_courses";

        } catch (ResponseStatusException e) {
            model.addAttribute("error", e.getReason());
            model.addAttribute("trainers", trainerService.getAllTrainers());
            return "create_course";
        }
    }
}
