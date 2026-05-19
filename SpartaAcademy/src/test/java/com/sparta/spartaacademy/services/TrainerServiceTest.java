package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.TrainerRequestDTO;
import com.sparta.spartaacademy.dtos.TrainerResponseDTO;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private TrainerRequestDTO requestDTO;
    private TrainerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainer = new Trainer();
        trainer.setTrainerId(1);
        trainer.setFirstName("Alice");
        trainer.setLastName("Smith");
        trainer.setEmail("alice@sparta.com");
        trainer.setPhoneNumber("07700000001");
        trainer.setRole("TRAINER");
        trainer.setCourses(List.of());

        requestDTO = new TrainerRequestDTO();
        requestDTO.setFirstName("Alice");
        requestDTO.setLastName("Smith");
        requestDTO.setEmail("alice@sparta.com");
        requestDTO.setPhoneNumber("07700000001");

        responseDTO = new TrainerResponseDTO(1, "Alice", "Smith",
                "alice@sparta.com", "07700000001", List.of());
    }

    // createTrainer

    @Test
    void createTrainer_validData_returnsResponseDTO() {
        when(trainerRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        TrainerResponseDTO result = trainerService.createTrainer(requestDTO);

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void createTrainer_duplicateEmail_throwsResponseStatusException() {
        when(trainerRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> trainerService.createTrainer(requestDTO));
        verify(trainerRepository, never()).save(any());
    }

    // getTrainerById

    @Test
    void getTrainerById_validId_returnsResponseDTO() {
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer));

        TrainerResponseDTO result = trainerService.getTrainerById(1);

        assertNotNull(result);
        assertEquals(1, result.getTrainerId());
        assertEquals("Alice", result.getFirstName());
    }

    @Test
    void getTrainerById_invalidId_throwsResponseStatusException() {
        when(trainerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> trainerService.getTrainerById(99));
    }

    // getAllTrainers

    @Test
    void getAllTrainers_returnsList() {
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));

        List<TrainerResponseDTO> result = trainerService.getAllTrainers();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
    }

    @Test
    void getAllTrainers_emptyList_returnsEmptyList() {
        when(trainerRepository.findAll()).thenReturn(List.of());

        List<TrainerResponseDTO> result = trainerService.getAllTrainers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // updateTrainer

    @Test
    void updateTrainer_validId_returnsUpdatedDTO() {
        TrainerRequestDTO updatedDTO = new TrainerRequestDTO();
        updatedDTO.setFirstName("Alicia");
        updatedDTO.setLastName("Smith");
        updatedDTO.setEmail("alicia@sparta.com");
        updatedDTO.setPhoneNumber("07700000099");

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setTrainerId(1);
        updatedTrainer.setFirstName("Alicia");
        updatedTrainer.setLastName("Smith");
        updatedTrainer.setEmail("alicia@sparta.com");
        updatedTrainer.setPhoneNumber("07700000099");
        updatedTrainer.setCourses(List.of());

        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(updatedTrainer);

        TrainerResponseDTO result = trainerService.updateTrainer(1, updatedDTO);

        assertNotNull(result);
        assertEquals("Alicia", result.getFirstName());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void updateTrainer_invalidId_throwsResponseStatusException() {
        when(trainerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> trainerService.updateTrainer(99, requestDTO));
        verify(trainerRepository, never()).save(any());
    }

    // deleteTrainer

    @Test
    void deleteTrainer_validId_deletesSuccessfully() {
        when(trainerRepository.existsById(1)).thenReturn(true);

        trainerService.deleteTrainer(1);

        verify(trainerRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteTrainer_invalidId_throwsResponseStatusException() {
        when(trainerRepository.existsById(99)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> trainerService.deleteTrainer(99));
        verify(trainerRepository, never()).deleteById(any());
    }
}
