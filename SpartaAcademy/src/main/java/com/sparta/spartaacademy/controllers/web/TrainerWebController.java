package com.sparta.spartaacademy.controllers.web;

import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.dtos.TraineeResponseDTO;
import com.sparta.spartaacademy.services.CourseService;
import com.sparta.spartaacademy.services.TraineeService;
import com.sparta.spartaacademy.services.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/trainer")
public class TrainerWebController
{
    private final TrainerService trainerservice;
    private final TraineeService traineeservice;
    private final CourseService courseservice;

    public TrainerWebController(TrainerService trainerservice, TraineeService traineeservice, CourseService courseservice)
    {
        this.trainerservice = trainerservice;
        this.traineeservice = traineeservice;
        this.courseservice = courseservice;
    }

        @GetMapping("/view_trainees")
        public String viewAllTrainees(
                @RequestParam(required = false) String keyword,
                Model model) {

            List<TraineeResponseDTO> trainees;

            if (keyword != null && !keyword.trim().isEmpty()) {

                trainees = traineeservice.searchTrainees(keyword);

            } else {

                trainees = traineeservice.getAllTrainees();
            }

            model.addAttribute("trainees", trainees);
            model.addAttribute("keyword", keyword);

            return "trainer/view_trainees";
        }

    @GetMapping("/view_courses")
    public String viewAllCourses(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<CourseResponseDTO> courses;

        if (keyword != null && !keyword.trim().isEmpty()) {

            courses = courseservice.searchCoursesByName(keyword);

        } else {

            courses = courseservice.getAllCourses();
        }

        model.addAttribute("courses", courses);
        model.addAttribute("keyword", keyword);

        return "trainer/view_courses";
    }
    }

