package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.TraineeMapper;
import com.sparta.spartaacademy.dtos.TraineeRequestDTO;
import com.sparta.spartaacademy.dtos.TraineeResponseDTO;
import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final PasswordEncoder passwordEncoder;

    public TraineeService(TraineeRepository traineeRepository,
                          TraineeMapper traineeMapper,
                          PasswordEncoder passwordEncoder) {
        this.traineeRepository = traineeRepository;
        this.traineeMapper = traineeMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public TraineeResponseDTO createTrainee(TraineeRequestDTO dto) {
        if (traineeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }
        Trainee trainee = traineeMapper.toEntity(dto);
        trainee.setPassword(passwordEncoder.encode(dto.getPassword()));
        return traineeMapper.toResponseDTO(traineeRepository.save(trainee));
    }

    public TraineeResponseDTO getTraineeById(Integer id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainee not found with id: " + id));
        return traineeMapper.toResponseDTO(trainee);
    }

    public List<TraineeResponseDTO> getAllTrainees() {
        return traineeRepository.findAll()
                .stream()
                .map(traineeMapper::toResponseDTO)
                .toList();
    }

    public TraineeResponseDTO updateTrainee(Integer id, TraineeRequestDTO dto) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainee not found with id: " + id));
        traineeMapper.updateEntity(dto, trainee);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            trainee.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return traineeMapper.toResponseDTO(traineeRepository.save(trainee));
    }

    public void deleteTrainee(Integer id) {
        if (!traineeRepository.existsById(id)) {
            throw new RuntimeException("Trainee not found with id: " + id);
        }
        traineeRepository.deleteById(id);
    }

    public List<TraineeResponseDTO> findByName(String firstName) {
        return traineeRepository.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(traineeMapper::toResponseDTO)
                .toList();
    }

    public List<TraineeResponseDTO> findByCourse(Integer courseId) {
        return traineeRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(traineeMapper::toResponseDTO)
                .toList();
    }

    public List<TraineeResponseDTO> searchTrainees(String keyword) {

        return traineeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourse_CourseNameContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        keyword
                )
                .stream()
                .map(traineeMapper::toResponseDTO)
                .toList();
    }
}

