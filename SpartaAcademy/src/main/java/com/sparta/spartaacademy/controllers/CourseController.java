package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "course-controller", description = "Endpoints for managing courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Create a new course", description = "Creates a new course. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "409", description = "Course already exists")
    })
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO responseDTO = courseService.createCourse(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Get all courses", description = "Returns all courses. Pass optional name param to search by name. Accessible by trainers and trainees.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of courses returned")
    })
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('TRAINER', 'TRAINEE')")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(@RequestParam(required = false) String courseName) {
        if (courseName != null && !courseName.isEmpty()) {
            return ResponseEntity.ok(courseService.searchCoursesByName(courseName));
        }
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(summary = "Get a course by ID", description = "Returns a single course by its ID. Accessible by trainers and trainees.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'TRAINEE')")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Update a course", description = "Updates an existing course by its ID. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Integer courseId,
                                                          @Valid @RequestBody CourseRequestDTO requestDTO) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, requestDTO));
    }

    @Operation(summary = "Delete a course", description = "Deletes a course by its ID. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        boolean deleted = courseService.deleteCourse(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Enrol a trainee on a course", description = "Enrols a trainee onto a course by their IDs. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee enrolled successfully"),
            @ApiResponse(responseCode = "404", description = "Course or trainee not found"),
            @ApiResponse(responseCode = "409", description = "Trainee already enrolled")
    })
    @PostMapping("/{courseId}/enrol")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<CourseResponseDTO> enrolTrainee(@PathVariable Integer courseId,
                                                          @RequestParam Integer traineeId) {
        CourseResponseDTO response = courseService.enrollTrainee(courseId, traineeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get courses by trainer", description = "Returns all courses assigned to a specific trainer. Accessible by trainers and trainees.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of courses returned"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/trainer/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'TRAINEE')")
    public ResponseEntity<List<CourseResponseDTO>> getTrainersCourses(@PathVariable Integer id) {
        List<CourseResponseDTO> coursesList = courseService.getTrainersCourses(id);
        return ResponseEntity.ok(coursesList);
    }
}
