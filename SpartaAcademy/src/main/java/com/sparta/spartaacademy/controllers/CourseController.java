package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.services.CourseService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {


    private final CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping("/course")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO){

        CourseResponseDTO responseDTO = courseService.createCourse(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(@RequestParam(required = false) String courseName){

        if(courseName != null && !courseName.isEmpty()){
            List<CourseResponseDTO> courses = courseService.searchCoursesByName(courseName);
            return ResponseEntity.ok(courses);
        }

        return ResponseEntity.ok(courseService.getAllCourses());
    }


    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Integer courseId,
                                                          @Valid @RequestBody CourseRequestDTO requestDTO){

        return ResponseEntity.ok(courseService.updateCourse(courseId, requestDTO));

    }

    @Operation(summary = "Delete an existing Course", description = "Delete a course from the database")
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteCourse(@PathVariable int id)
    {
        boolean deletecourse = courseService.deleteCourse(id);
        if (deletecourse)
        {
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
