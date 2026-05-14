package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.TraineeDTO;
import com.sparta.spartaacademy.services.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@Tag(name = "trainee-controller", description = "Endpoints for managing trainees")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Operation(summary = "Create a new trainee", description = "Registers a new trainee in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    public ResponseEntity<TraineeDTO> createTrainee(@Valid @RequestBody TraineeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.createTrainee(dto));
    }

    @Operation(summary = "Get all trainees", description = "Returns a list of all registered trainees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully")
    })
    @GetMapping
    public ResponseEntity<List<TraineeDTO>> getAllTrainees() {
        return ResponseEntity.ok(traineeService.getAllTrainees());
    }

    @Operation(summary = "Get a trainee by ID", description = "Returns a single trainee by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TraineeDTO> getTraineeById(@PathVariable Integer id) {
        return ResponseEntity.ok(traineeService.getTraineeById(id));
    }

    @Operation(summary = "Search trainees by first name", description = "Returns all trainees whose first name contains the given string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<TraineeDTO>> searchByName(@RequestParam String firstName) {
        return ResponseEntity.ok(traineeService.findByName(firstName));
    }

    @Operation(summary = "Get trainees by course", description = "Returns all trainees enrolled on a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TraineeDTO>> getTraineesByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(traineeService.findByCourse(courseId));
    }

    @Operation(summary = "Update a trainee", description = "Updates the details of an existing trainee by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TraineeDTO> updateTrainee(@PathVariable Integer id, @Valid @RequestBody TraineeDTO dto) {
        return ResponseEntity.ok(traineeService.updateTrainee(id, dto));
    }

    @Operation(summary = "Delete a trainee", description = "Removes a trainee from the system by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable Integer id) {
        traineeService.deleteTrainee(id);
        return ResponseEntity.noContent().build();
    }
}
