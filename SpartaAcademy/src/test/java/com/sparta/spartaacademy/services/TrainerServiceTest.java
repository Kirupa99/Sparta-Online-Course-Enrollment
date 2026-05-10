package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.TrainerRequestDTO;
import com.sparta.spartaacademy.dtos.TrainerResponseDTO;
import com.sparta.spartaacademy.entities.Course;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerRequestDTO requestDTO;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        requestDTO = new TrainerRequestDTO(
                "Curtis", "Logan", "curtis.logan@example.com", "01254123456");
        trainer = new Trainer(
                "Curtis", "Logan", "curtis.logan@example.com", "01254123456");
        trainer.setTrainerId(1);
    }

    @Test
    void createTrainer_savesAndReturnsDTO() {
        when(trainerRepository.existsByEmail("curtis.logan@example.com")).thenReturn(false);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        TrainerResponseDTO result = trainerService.createTrainer(requestDTO);

        assertEquals(1, result.getTrainerId());
        assertEquals("Curtis", result.getFirstName());
        assertEquals("Logan", result.getLastName());
        assertEquals("curtis.logan@example.com", result.getEmail());
        assertEquals("01254123456", result.getPhoneNumber());
        assertEquals(List.of(), result.getCourseIds());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void createTrainer_throwsWhenEmailExists() {
        when(trainerRepository.existsByEmail("curtis.logan@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> trainerService.createTrainer(requestDTO)
        );
        assertTrue(ex.getMessage().contains("Email already exists"));
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void getTrainerById_returnsDTO() {
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer));

        TrainerResponseDTO result = trainerService.getTrainerById(1);

        assertEquals(1, result.getTrainerId());
        assertEquals("Curtis", result.getFirstName());
    }

    @Test
    void getTrainerById_includesCourseIds() {
        Course c1 = mock(Course.class);
        when(c1.getCourseId()).thenReturn(10);
        Course c2 = mock(Course.class);
        when(c2.getCourseId()).thenReturn(20);
        trainer.setCourses(List.of(c1, c2));
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer));

        TrainerResponseDTO result = trainerService.getTrainerById(1);

        assertEquals(List.of(10, 20), result.getCourseIds());
    }

    @Test
    void getTrainerById_throwsWhenMissing() {
        when(trainerRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> trainerService.getTrainerById(99)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void getAllTrainers_returnsList() {
        Trainer second = new Trainer(
                "Jane", "Lancaster", "jane.lancaster@example.com", "01524000111");
        second.setTrainerId(2);
        when(trainerRepository.findAll()).thenReturn(List.of(trainer, second));

        List<TrainerResponseDTO> result = trainerService.getAllTrainers();

        assertEquals(2, result.size());
        assertEquals("Curtis", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void updateTrainer_savesAndReturnsDTO() {
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));
        TrainerRequestDTO updateDTO = new TrainerRequestDTO(
                "Curtis", "Logan", "curtis.logan@lancashire.com", "01772999888");

        TrainerResponseDTO result = trainerService.updateTrainer(1, updateDTO);

        assertEquals(1, result.getTrainerId());
        assertEquals("Curtis", result.getFirstName());
        assertEquals("curtis.logan@lancashire.com", result.getEmail());
        assertEquals("01772999888", result.getPhoneNumber());
    }

    @Test
    void updateTrainer_throwsWhenMissing() {
        when(trainerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> trainerService.updateTrainer(99, requestDTO));
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void deleteTrainer_callsRepository() {
        when(trainerRepository.existsById(1)).thenReturn(true);

        trainerService.deleteTrainer(1);

        verify(trainerRepository).deleteById(1);
    }

    @Test
    void deleteTrainer_throwsWhenMissing() {
        when(trainerRepository.existsById(99)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> trainerService.deleteTrainer(99));
        verify(trainerRepository, never()).deleteById(anyInt());
    }
}
