package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.TrainerRequestDTO;
import com.sparta.spartaacademy.dtos.TrainerResponseDTO;
import com.sparta.spartaacademy.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "trainer-controller", description = "Endpoints for managing trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Operation(summary = "Create a new trainer", description = "Registers a new trainer. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('TRAINER')")
    public TrainerResponseDTO createTrainer(@Valid @RequestBody TrainerRequestDTO dto) {
        return trainerService.createTrainer(dto);
    }

    @Operation(summary = "Get all trainers", description = "Returns a list of all trainers. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned successfully")
    })
    @GetMapping
    @PreAuthorize("hasRole('TRAINER')")
    public List<TrainerResponseDTO> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @Operation(summary = "Get a trainer by ID", description = "Returns a single trainer by their ID. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public TrainerResponseDTO getTrainerById(@PathVariable Integer id) {
        return trainerService.getTrainerById(id);
    }

    @Operation(summary = "Update a trainer", description = "Updates the details of an existing trainer. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public TrainerResponseDTO updateTrainer(@PathVariable Integer id,
                                            @Valid @RequestBody TrainerRequestDTO dto) {
        return trainerService.updateTrainer(id, dto);
    }

    @Operation(summary = "Delete a trainer", description = "Removes a trainer from the system. Trainers only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('TRAINER')")
    public void deleteTrainer(@PathVariable Integer id) {
        trainerService.deleteTrainer(id);
    }
}
