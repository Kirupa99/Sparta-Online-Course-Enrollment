package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.TraineeDTO;
import com.sparta.spartaacademy.services.TraineeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public ResponseEntity<TraineeDTO> createTrainee(@Valid @RequestBody TraineeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.createTrainee(dto));
    }

    @GetMapping
    public ResponseEntity<List<TraineeDTO>> getAllTrainees() {
        return ResponseEntity.ok(traineeService.getAllTrainees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TraineeDTO> getTraineeById(@PathVariable Integer id) {
        return ResponseEntity.ok(traineeService.getTraineeById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TraineeDTO>> searchByName(@RequestParam String firstName) {
        return ResponseEntity.ok(traineeService.findByName(firstName));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TraineeDTO>> getTraineesByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(traineeService.findByCourse(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TraineeDTO> updateTrainee(@PathVariable Integer id, @Valid @RequestBody TraineeDTO dto) {
        return ResponseEntity.ok(traineeService.updateTrainee(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable Integer id) {
        traineeService.deleteTrainee(id);
        return ResponseEntity.noContent().build();
    }
}
