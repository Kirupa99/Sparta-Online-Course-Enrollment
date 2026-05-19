package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.entities.Course;
import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.CourseRepository;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private CourseRequestDTO requestDTO;
    private Trainer trainer;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainer = new Trainer();
        trainer.setTrainerId(1);
        trainer.setFirstName("Alice");
        trainer.setLastName("Smith");
        trainer.setEmail("alice@sparta.com");
        trainer.setCourses(new ArrayList<>());

        trainee = new Trainee();
        trainee.setTraineeId(1);
        trainee.setFirstName("Charlie");
        trainee.setLastName("Brown");
        trainee.setEmail("charlie@sparta.com");

        course = new Course();
        course.setCourseName("Java Development");
        course.setDescription("Advanced Java");
        course.setStartDate(LocalDate.of(2026, 4, 10));
        course.setEndDate(LocalDate.of(2026, 8, 10));
        course.setMaxStudents(20);
        course.setTrainers(new ArrayList<>(List.of(trainer)));
        course.setTrainees(new ArrayList<>());

        requestDTO = new CourseRequestDTO();
        requestDTO.setCourseName("Java Development");
        requestDTO.setDescription("Advanced Java");
        requestDTO.setStartDate(LocalDate.of(2026, 4, 10));
        requestDTO.setEndDate(LocalDate.of(2026, 8, 10));
        requestDTO.setMaxStudents(20);
    }

    // createCourse

    @Test
    void createCourse_validData_returnsResponseDTO() {
        when(courseRepository.existsByCourseName(requestDTO.getCourseName())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseResponseDTO result = courseService.createCourse(requestDTO);

        assertNotNull(result);
        assertEquals("Java Development", result.getCourseName());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void createCourse_duplicateName_throwsResponseStatusException() {
        when(courseRepository.existsByCourseName(requestDTO.getCourseName())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> courseService.createCourse(requestDTO));
        verify(courseRepository, never()).save(any());
    }

    // getCourseById

    @Test
    void getCourseById_validId_returnsResponseDTO() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        CourseResponseDTO result = courseService.getCourseById(1);

        assertNotNull(result);
        assertEquals("Java Development", result.getCourseName());
    }

    @Test
    void getCourseById_invalidId_throwsResponseStatusException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.getCourseById(99));
    }

    // getAllCourses

    @Test
    void getAllCourses_returnsList() {
        when(courseRepository.findAll()).thenReturn(List.of(course));

        List<CourseResponseDTO> result = courseService.getAllCourses();

        assertEquals(1, result.size());
        assertEquals("Java Development", result.get(0).getCourseName());
    }

    @Test
    void getAllCourses_emptyList_returnsEmptyList() {
        when(courseRepository.findAll()).thenReturn(List.of());

        List<CourseResponseDTO> result = courseService.getAllCourses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // searchCoursesByName

    @Test
    void searchCoursesByName_matchingResults_returnsList() {
        when(courseRepository.findCoursesByCourseNameContainingIgnoreCase("Java")).thenReturn(List.of(course));

        List<CourseResponseDTO> result = courseService.searchCoursesByName("Java");

        assertEquals(1, result.size());
        assertEquals("Java Development", result.get(0).getCourseName());
    }

    @Test
    void searchCoursesByName_noMatches_returnsEmptyList() {
        when(courseRepository.findCoursesByCourseNameContainingIgnoreCase("Unknown")).thenReturn(List.of());

        List<CourseResponseDTO> result = courseService.searchCoursesByName("Unknown");

        assertTrue(result.isEmpty());
    }

    // updateCourse

    @Test
    void updateCourse_validId_returnsUpdatedDTO() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseResponseDTO result = courseService.updateCourse(1, requestDTO);

        assertNotNull(result);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourse_invalidId_throwsResponseStatusException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.updateCourse(99, requestDTO));
        verify(courseRepository, never()).save(any());
    }

    // deleteCourse

    @Test
    void deleteCourse_validId_returnsTrue() {
        course.setTrainees(new ArrayList<>());
        course.setTrainers(new ArrayList<>());
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        boolean result = courseService.deleteCourse(1);

        assertTrue(result);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void deleteCourse_invalidId_returnsFalse() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        boolean result = courseService.deleteCourse(99);

        assertFalse(result);
        verify(courseRepository, never()).delete(any());
    }

    @Test
    void deleteCourse_withTrainees_setsTraineeCourseToNull() {
        trainee.setCourse(course);
        course.setTrainees(new ArrayList<>(List.of(trainee)));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1);

        assertNull(trainee.getCourse());
        verify(traineeRepository, times(1)).saveAll(anyList());
    }

    // enrollTrainee

    @Test
    void enrollTrainee_validIds_returnsUpdatedCourse() {
        trainee.setCourse(null);
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        CourseResponseDTO result = courseService.enrollTrainee(1, 1);

        assertNotNull(result);
        assertEquals(course, trainee.getCourse());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void enrollTrainee_invalidCourseId_throwsResponseStatusException() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.enrollTrainee(99, 1));
    }

    @Test
    void enrollTrainee_invalidTraineeId_throwsResponseStatusException() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(traineeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.enrollTrainee(1, 99));
    }

    // getTrainersCourses

    @Test
    void getTrainersCourses_validTrainerId_returnsList() {
        trainer.setCourses(List.of(course));
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer));

        List<CourseResponseDTO> result = courseService.getTrainersCourses(1);

        assertEquals(1, result.size());
        assertEquals("Java Development", result.get(0).getCourseName());
    }

    @Test
    void getTrainersCourses_invalidTrainerId_throwsResponseStatusException() {
        when(trainerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> courseService.getTrainersCourses(99));
    }

    @Test
    void getTrainersCourses_noCoursesAssigned_returnsEmptyList() {
        trainer.setCourses(List.of());
        when(trainerRepository.findById(1)).thenReturn(Optional.of(trainer));

        List<CourseResponseDTO> result = courseService.getTrainersCourses(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}