package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.TraineeRequestDTO;
import com.sparta.spartaacademy.dtos.TraineeResponseDTO;
import com.sparta.spartaacademy.services.TraineeService;
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
@RequestMapping("/api/trainees")
@Tag(name = "trainee-controller", description = "Endpoints for managing trainees")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Operation(summary = "Create a new trainee", description = "Registers a new trainee. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TraineeResponseDTO> createTrainee(@Valid @RequestBody TraineeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.createTrainee(dto));
    }

    @Operation(summary = "Get all trainees", description = "Returns all trainees. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully")
    })
    @GetMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<TraineeResponseDTO>> getAllTrainees() {
        return ResponseEntity.ok(traineeService.getAllTrainees());
    }

    @Operation(summary = "Get a trainee by ID", description = "Trainers can view any trainee; trainees can only view themselves.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER') or (hasRole('TRAINEE') and #id == authentication.principal.traineeId)")
    public ResponseEntity<TraineeResponseDTO> getTraineeById(@PathVariable Integer id) {
        return ResponseEntity.ok(traineeService.getTraineeById(id));
    }

    @Operation(summary = "Search trainees by first name", description = "Returns trainees matching the given first name. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<TraineeResponseDTO>> searchByName(@RequestParam String firstName) {
        return ResponseEntity.ok(traineeService.findByName(firstName));
    }

    @Operation(summary = "Get trainees by course", description = "Returns all trainees on a specific course. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<TraineeResponseDTO>> getTraineesByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(traineeService.findByCourse(courseId));
    }

    @Operation(summary = "Update a trainee", description = "Trainers can update anyone; trainees can only update themselves.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER') or (hasRole('TRAINEE') and #id == authentication.principal.traineeId)")
    public ResponseEntity<TraineeResponseDTO> updateTrainee(@PathVariable Integer id,
                                                            @Valid @RequestBody TraineeRequestDTO dto) {
        return ResponseEntity.ok(traineeService.updateTrainee(id, dto));
    }

    @Operation(summary = "Delete a trainee", description = "Removes a trainee from the system. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> deleteTrainee(@PathVariable Integer id) {
        traineeService.deleteTrainee(id);
        return ResponseEntity.noContent().build();
    }
}

