package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.TraineeDTO;
import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;

    public TraineeService(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public TraineeDTO createTrainee(TraineeDTO dto) {
        if (traineeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }
        Trainee trainee = mapToEntity(dto);
        return mapToDTO(traineeRepository.save(trainee));
    }

    public TraineeDTO getTraineeById(Integer id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainee not found with id: " + id));
        return mapToDTO(trainee);
    }

    public List<TraineeDTO> getAllTrainees() {
        return traineeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TraineeDTO updateTrainee(Integer id, TraineeDTO dto) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainee not found with id: " + id));
        trainee.setFirstName(dto.getFirstName());
        trainee.setLastName(dto.getLastName());
        trainee.setEmail(dto.getEmail());
        trainee.setCity(dto.getCity());
        trainee.setEnrolledDate(dto.getEnrolledDate());
        return mapToDTO(traineeRepository.save(trainee));
    }

    public void deleteTrainee(Integer id) {
        if (!traineeRepository.existsById(id)) {
            throw new RuntimeException("Trainee not found with id: " + id);
        }
        traineeRepository.deleteById(id);
    }

    public List<TraineeDTO> findByName(String firstName) {
        return traineeRepository.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<TraineeDTO> findByCourse(Integer courseId) {
        return traineeRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private TraineeDTO mapToDTO(Trainee trainee) {
        return new TraineeDTO(
                trainee.getTraineeId(),
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getEmail(),
                trainee.getCity(),
                trainee.getEnrolledDate(),
                trainee.getCourse() != null ? trainee.getCourse().getCourseId() : null
        );
    }

    private Trainee mapToEntity(TraineeDTO dto) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(dto.getFirstName());
        trainee.setLastName(dto.getLastName());
        trainee.setEmail(dto.getEmail());
        trainee.setCity(dto.getCity());
        trainee.setEnrolledDate(dto.getEnrolledDate());
        return trainee;
    }
}
