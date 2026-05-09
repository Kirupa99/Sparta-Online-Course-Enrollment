package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.services.CourseService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new course",
            description = "Creates a new course. Returns 409 if course already exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "409", description = "Course already exists")
    })
    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO){

        CourseResponseDTO responseDTO = courseService.createCourse(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @Operation(summary = "Get all courses",
            description = "Returns all courses. Pass optional name param to search by name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of courses returned")
    })
    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(@RequestParam(required = false) String courseName){

        if(courseName != null && !courseName.isEmpty()){
            List<CourseResponseDTO> courses = courseService.searchCoursesByName(courseName);
            return ResponseEntity.ok(courses);
        }

        return ResponseEntity.ok(courseService.getAllCourses());
    }


    @Operation(summary = "Update a course",
            description = "Updates an existing course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Integer courseId,
                                                          @Valid @RequestBody CourseRequestDTO requestDTO){

        return ResponseEntity.ok(courseService.updateCourse(courseId, requestDTO));

    }


    @Operation(summary = "Get a course by ID",
            description = "Returns a single course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
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


    @PostMapping("/{courseId}/enrol")
    public ResponseEntity<CourseResponseDTO> enrolTrainee(@PathVariable Integer courseId,
                                                          @RequestParam Integer traineeId){

        CourseResponseDTO response = courseService.enrollTrainee(courseId, traineeId);
        return  ResponseEntity.ok(response);
    }


    //view the trainers assigned courses
    @GetMapping("/trainer/{id}")
    public ResponseEntity<List<CourseResponseDTO>> getTrainersCourses(@PathVariable Integer id){

        List<CourseResponseDTO> coursesList = courseService.getTrainersCourses(id);
        return ResponseEntity.ok(coursesList);
    }
}
