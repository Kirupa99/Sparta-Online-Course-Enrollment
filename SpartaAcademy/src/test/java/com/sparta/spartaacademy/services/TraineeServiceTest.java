package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.TraineeDTO;
import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private TraineeDTO traineeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainee = new Trainee();
        trainee.setTraineeId(1);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setEmail("john.doe@email.com");
        trainee.setCity("London");
        trainee.setEnrolledDate(LocalDate.of(2024, 1, 15));
        trainee.setCourse(null);

        traineeDTO = new TraineeDTO(
                1,
                "John",
                "Doe",
                "john.doe@email.com",
                "London",
                LocalDate.of(2024, 1, 15),
                null
        );
    }

    // -------------------------
    // createTrainee
    // -------------------------

    @Test
    void createTrainee_validData_returnsDTO() {
        when(traineeRepository.existsByEmail(traineeDTO.getEmail())).thenReturn(false);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        TraineeDTO result = traineeService.createTrainee(traineeDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@email.com", result.getEmail());
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void createTrainee_duplicateEmail_throwsIllegalArgumentException() {
        when(traineeRepository.existsByEmail(traineeDTO.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> traineeService.createTrainee(traineeDTO));
        verify(traineeRepository, never()).save(any(Trainee.class));
    }

    // -------------------------
    // getTraineeById
    // -------------------------

    @Test
    void getTraineeById_validId_returnsDTO() {
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));

        TraineeDTO result = traineeService.getTraineeById(1);

        assertNotNull(result);
        assertEquals(1, result.getTraineeId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void getTraineeById_invalidId_throwsRuntimeException() {
        when(traineeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> traineeService.getTraineeById(99));
    }

    // -------------------------
    // getAllTrainees
    // -------------------------

    @Test
    void getAllTrainees_withTrainees_returnsList() {
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));

        List<TraineeDTO> result = traineeService.getAllTrainees();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void getAllTrainees_noTrainees_returnsEmptyList() {
        when(traineeRepository.findAll()).thenReturn(List.of());

        List<TraineeDTO> result = traineeService.getAllTrainees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------
    // updateTrainee
    // -------------------------

    @Test
    void updateTrainee_validId_returnsUpdatedDTO() {
        TraineeDTO updatedDTO = new TraineeDTO(
                1,
                "Jane",
                "Doe",
                "jane.doe@email.com",
                "Manchester",
                LocalDate.of(2024, 3, 10),
                null
        );

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setTraineeId(1);
        updatedTrainee.setFirstName("Jane");
        updatedTrainee.setLastName("Doe");
        updatedTrainee.setEmail("jane.doe@email.com");
        updatedTrainee.setCity("Manchester");
        updatedTrainee.setEnrolledDate(LocalDate.of(2024, 3, 10));
        updatedTrainee.setCourse(null);

        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(updatedTrainee);

        TraineeDTO result = traineeService.updateTrainee(1, updatedDTO);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Manchester", result.getCity());
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void updateTrainee_invalidId_throwsRuntimeException() {
        when(traineeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> traineeService.updateTrainee(99, traineeDTO));
        verify(traineeRepository, never()).save(any(Trainee.class));
    }

    // -------------------------
    // deleteTrainee
    // -------------------------

    @Test
    void deleteTrainee_validId_deletesSuccessfully() {
        when(traineeRepository.existsById(1)).thenReturn(true);
        doNothing().when(traineeRepository).deleteById(1);

        traineeService.deleteTrainee(1);

        verify(traineeRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteTrainee_invalidId_throwsRuntimeException() {
        when(traineeRepository.existsById(99)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.deleteTrainee(99));
        verify(traineeRepository, never()).deleteById(any());
    }

    // -------------------------
    // findByName
    // -------------------------

    @Test
    void findByName_matchingResults_returnsList() {
        when(traineeRepository.findByFirstNameContainingIgnoreCase("John")).thenReturn(List.of(trainee));

        List<TraineeDTO> result = traineeService.findByName("John");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void findByName_noMatches_returnsEmptyList() {
        when(traineeRepository.findByFirstNameContainingIgnoreCase("Unknown")).thenReturn(List.of());

        List<TraineeDTO> result = traineeService.findByName("Unknown");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------
    // findByCourse
    // -------------------------

    @Test
    void findByCourse_validCourseId_returnsList() {
        when(traineeRepository.findByCourse_CourseId(1)).thenReturn(List.of(trainee));

        List<TraineeDTO> result = traineeService.findByCourse(1);

        assertEquals(1, result.size());
    }

    @Test
    void findByCourse_noTraineesOnCourse_returnsEmptyList() {
        when(traineeRepository.findByCourse_CourseId(99)).thenReturn(List.of());

        List<TraineeDTO> result = traineeService.findByCourse(99);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
