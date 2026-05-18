package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.TrainerProfileUpdateDTO;
import com.sparta.spartaacademy.dtos.TrainerRequestDTO;
import com.sparta.spartaacademy.dtos.TrainerResponseDTO;
import com.sparta.spartaacademy.entities.Course;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;

    public TrainerService(TrainerRepository trainerRepository, PasswordEncoder passwordEncoder) {

        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TrainerResponseDTO createTrainer(TrainerRequestDTO dto) {
        if (trainerRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + dto.getEmail());
        }
        Trainer trainer = mapToEntity(dto);
        return mapToDTO(trainerRepository.save(trainer));
    }

    public TrainerResponseDTO getTrainerById(Integer id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found with id: " + id));
        return mapToDTO(trainer);
    }

    public List<TrainerResponseDTO> getAllTrainers() {
        return trainerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TrainerResponseDTO updateTrainer(Integer id, TrainerRequestDTO dto) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found with id: " + id));
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainer.setEmail(dto.getEmail());
        trainer.setPhoneNumber(dto.getPhoneNumber());
        return mapToDTO(trainerRepository.save(trainer));
    }

    public void deleteTrainer(Integer id) {
        if (!trainerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found with id: " + id);
        }
        trainerRepository.deleteById(id);
    }

    private TrainerResponseDTO mapToDTO(Trainer trainer) {
        List<Integer> courseIds = (trainer.getCourses() == null)
                ? Collections.emptyList()
                : trainer.getCourses().stream().map(Course::getCourseId).toList();
        return new TrainerResponseDTO(
                trainer.getTrainerId(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getEmail(),
                trainer.getPhoneNumber(),
                courseIds
        );
    }

    private Trainer mapToEntity(TrainerRequestDTO dto) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainer.setEmail(dto.getEmail());
        trainer.setPhoneNumber(dto.getPhoneNumber());
        return trainer;
    }


    public Trainer findByEmail(String email) {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
    }


    public Trainer updateProfile(String loggedInEmail, TrainerProfileUpdateDTO dto) {
        Trainer trainer = trainerRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));

        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainer.setEmail(dto.getEmail());
        trainer.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            trainer.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return trainerRepository.save(trainer);
    }
}
