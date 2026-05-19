package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.TraineeMapper;
import com.sparta.spartaacademy.dtos.TraineeRequestDTO;
import com.sparta.spartaacademy.dtos.TraineeResponseDTO;
import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private TraineeRequestDTO requestDTO;
    private TraineeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainee = new Trainee();
        trainee.setTraineeId(1);
        trainee.setFirstName("Charlie");
        trainee.setLastName("Brown");
        trainee.setEmail("charlie@sparta.com");
        trainee.setCity("London");
        trainee.setEnrolledDate(LocalDate.of(2026, 4, 10));

        requestDTO = new TraineeRequestDTO();
        requestDTO.setFirstName("Charlie");
        requestDTO.setLastName("Brown");
        requestDTO.setEmail("charlie@sparta.com");
        requestDTO.setCity("London");
        requestDTO.setPassword("charliepass");

        responseDTO = new TraineeResponseDTO();
        responseDTO.setTraineeId(1);
        responseDTO.setFirstName("Charlie");
        responseDTO.setLastName("Brown");
        responseDTO.setEmail("charlie@sparta.com");
        responseDTO.setCity("London");
    }

    // createTrainee

    @Test
    void createTrainee_validData_returnsResponseDTO() {
        when(traineeRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(traineeMapper.toEntity(requestDTO)).thenReturn(trainee);
        when(passwordEncoder.encode("charliepass")).thenReturn("encodedpass");
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        TraineeResponseDTO result = traineeService.createTrainee(requestDTO);

        assertNotNull(result);
        assertEquals("Charlie", result.getFirstName());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void createTrainee_duplicateEmail_throwsIllegalArgumentException() {
        when(traineeRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> traineeService.createTrainee(requestDTO));
        verify(traineeRepository, never()).save(any());
    }

    // getTraineeById

    @Test
    void getTraineeById_validId_returnsResponseDTO() {
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        TraineeResponseDTO result = traineeService.getTraineeById(1);

        assertNotNull(result);
        assertEquals(1, result.getTraineeId());
    }

    @Test
    void getTraineeById_invalidId_throwsRuntimeException() {
        when(traineeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> traineeService.getTraineeById(99));
    }

    // getAllTrainees

    @Test
    void getAllTrainees_returnsList() {
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        List<TraineeResponseDTO> result = traineeService.getAllTrainees();

        assertEquals(1, result.size());
        assertEquals("Charlie", result.get(0).getFirstName());
    }

    @Test
    void getAllTrainees_emptyList_returnsEmptyList() {
        when(traineeRepository.findAll()).thenReturn(List.of());

        List<TraineeResponseDTO> result = traineeService.getAllTrainees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // updateTrainee

    @Test
    void updateTrainee_validId_returnsUpdatedDTO() {
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        TraineeResponseDTO result = traineeService.updateTrainee(1, requestDTO);

        assertNotNull(result);
        verify(traineeMapper, times(1)).updateEntity(requestDTO, trainee);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void updateTrainee_invalidId_throwsRuntimeException() {
        when(traineeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> traineeService.updateTrainee(99, requestDTO));
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void updateTrainee_withNewPassword_encodesPassword() {
        requestDTO.setPassword("newpassword");
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodednewpassword");
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        traineeService.updateTrainee(1, requestDTO);

        verify(passwordEncoder, times(1)).encode("newpassword");
    }

    @Test
    void updateTrainee_withBlankPassword_skipsEncoding() {
        requestDTO.setPassword("");
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        traineeService.updateTrainee(1, requestDTO);

        verify(passwordEncoder, never()).encode(any());
    }

    // deleteTrainee

    @Test
    void deleteTrainee_validId_deletesSuccessfully() {
        when(traineeRepository.existsById(1)).thenReturn(true);

        traineeService.deleteTrainee(1);

        verify(traineeRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteTrainee_invalidId_throwsRuntimeException() {
        when(traineeRepository.existsById(99)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.deleteTrainee(99));
        verify(traineeRepository, never()).deleteById(any());
    }

    // findByName

    @Test
    void findByName_matchingResults_returnsList() {
        when(traineeRepository.findByFirstNameContainingIgnoreCase("Charlie")).thenReturn(List.of(trainee));
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        List<TraineeResponseDTO> result = traineeService.findByName("Charlie");

        assertEquals(1, result.size());
        assertEquals("Charlie", result.get(0).getFirstName());
    }

    @Test
    void findByName_noMatches_returnsEmptyList() {
        when(traineeRepository.findByFirstNameContainingIgnoreCase("Unknown")).thenReturn(List.of());

        List<TraineeResponseDTO> result = traineeService.findByName("Unknown");

        assertTrue(result.isEmpty());
    }

    // findByCourse

    @Test
    void findByCourse_validCourseId_returnsList() {
        when(traineeRepository.findByCourse_CourseId(1)).thenReturn(List.of(trainee));
        when(traineeMapper.toResponseDTO(trainee)).thenReturn(responseDTO);

        List<TraineeResponseDTO> result = traineeService.findByCourse(1);

        assertEquals(1, result.size());
    }

    @Test
    void findByCourse_noTrainees_returnsEmptyList() {
        when(traineeRepository.findByCourse_CourseId(99)).thenReturn(List.of());

        List<TraineeResponseDTO> result = traineeService.findByCourse(99);

        assertTrue(result.isEmpty());
    }
}
