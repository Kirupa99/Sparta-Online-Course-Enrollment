package com.sparta.spartaacademy.controllers.web;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trainer")
public class TrainerWebController {

    private final TrainerService trainerservice;
    private final TraineeService traineeservice;
    private final CourseService courseservice;

    public TrainerWebController(TrainerService trainerservice, TraineeService traineeservice, CourseService courseservice) {
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
    public String viewAllCourses(@RequestParam(required = false) String keyword,
                                 Model model) {

        List<CourseResponseDTO> courses =
                keyword != null && !keyword.trim().isEmpty()
                        ? courseservice.searchCoursesByName(keyword)
                        : courseservice.getAllCourses();

        model.addAttribute("courses", courses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isTrainer", true);

        return "trainer/view_courses";
    }

    @GetMapping("/mycourses")
    public String viewTrainerCourses(Model model,
                                     Authentication authentication) {

        String email = authentication.getName();

        List<CourseResponseDTO> courses =
                courseservice.getCoursesForTrainer(email);

        model.addAttribute("courses", courses);
        model.addAttribute("isTrainer", true);
        model.addAttribute("isMyCourses", true);

        return "trainer/view_courses";
    }

    @GetMapping("/courses/{id}")
    public String viewCourseDetail(@PathVariable Integer id, Model model) {
        CourseResponseDTO course = courseservice.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("isTrainer", true);
        return "trainer/view_course_detail";
    }

    @GetMapping("/edit_course/{id}")
    public String showEditCourseForm(@PathVariable Integer id, Model model) {
        CourseResponseDTO course = courseservice.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("trainers", trainerservice.getAllTrainers());
        return "trainer/edit_course";
    }

    @PostMapping("/edit_course/{id}")
    public String updateCourse(@PathVariable Integer id,
                               @RequestParam String courseName,
                               @RequestParam(required = false) String description,
                               @RequestParam String startDate,
                               @RequestParam String endDate,
                               @RequestParam(required = false) Integer maxStudents,
                               @RequestParam(required = false) List<Integer> trainerIds,
                               Model model) {
        try {
            CourseRequestDTO requestDTO = new CourseRequestDTO();
            requestDTO.setCourseName(courseName);
            requestDTO.setDescription(description);
            requestDTO.setStartDate(java.time.LocalDate.parse(startDate));
            requestDTO.setEndDate(java.time.LocalDate.parse(endDate));
            requestDTO.setMaxStudents(maxStudents);
            requestDTO.setTrainerIds(trainerIds);
            courseservice.updateCourse(id, requestDTO);
            return "redirect:/trainer/view_courses";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update course: " + e.getMessage());
            CourseResponseDTO course = courseservice.getCourseById(id);
            model.addAttribute("course", course);
            model.addAttribute("trainers", trainerservice.getAllTrainers());
            return "trainer/edit_course";
        }
    }
}