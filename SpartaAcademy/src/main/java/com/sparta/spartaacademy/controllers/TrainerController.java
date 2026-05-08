package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.TrainerRequestDTO;
import com.sparta.spartaacademy.dtos.TrainerResponseDTO;
import com.sparta.spartaacademy.services.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerResponseDTO createTrainer(@Valid @RequestBody TrainerRequestDTO dto) {
        return trainerService.createTrainer(dto);
    }

    @GetMapping
    public List<TrainerResponseDTO> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @GetMapping("/{id}")
    public TrainerResponseDTO getTrainerById(@PathVariable Integer id) {
        return trainerService.getTrainerById(id);
    }

    @PutMapping("/{id}")
    public TrainerResponseDTO updateTrainer(@PathVariable Integer id,
                                            @Valid @RequestBody TrainerRequestDTO dto) {
        return trainerService.updateTrainer(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainer(@PathVariable Integer id) {
        trainerService.deleteTrainer(id);
    }
}
