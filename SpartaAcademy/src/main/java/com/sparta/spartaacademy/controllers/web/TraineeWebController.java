package com.sparta.spartaacademy.controllers.web;


import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.dtos.TraineeResponseDTO;
import com.sparta.spartaacademy.services.CourseService;
import com.sparta.spartaacademy.services.TraineeService;
import com.sparta.spartaacademy.services.TrainerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/trainees")
public class TraineeWebController
{
    private final TraineeService traineeservice;
    private final CourseService courseservice;

    public TraineeWebController(TraineeService traineeservice, CourseService courseservice)
    {
        this.traineeservice = traineeservice;
        this.courseservice = courseservice;
    }

    @GetMapping("/courses")
    public String viewTraineeCourses(Model model,
                                     Authentication authentication) {

        String email = authentication.getName();

        List<CourseResponseDTO> courses = courseservice.getCoursesForTrainee(email);

        model.addAttribute("courses", courses);
        model.addAttribute("isTrainer", false);
        model.addAttribute("isMyCourses", true);

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

