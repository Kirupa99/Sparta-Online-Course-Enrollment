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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock CourseRepository courseRepository;
    @Mock TrainerRepository trainerRepository;
    @Mock TraineeRepository traineeRepository;

    @InjectMocks CourseService courseService;

    Trainer john;
    Trainer sarah;
    Course javaCourse;
    Course cSharpCourse;
    CourseRequestDTO javaRequest;

    @BeforeEach
    void setUp() {
        john = trainer("John", "Smith");
        sarah = trainer("Sarah", "Jones");

        javaCourse = course(1, "Java Development", "Java course", 20, List.of(john));
        cSharpCourse = course(2, "C# Development", "C# course", 15, List.of(sarah));

        javaRequest = request("Java Development", "Java course", 20, List.of(1));
    }

    @Test
    void createCourse_ShouldCreateCourse_WhenCourseDoesNotAlreadyExist() {
        when(courseRepository.existsByCourseName("Java Development")).thenReturn(false);
        when(trainerRepository.findAllById(List.of(1))).thenReturn(List.of(john));
        when(courseRepository.save(any(Course.class))).thenReturn(javaCourse);

        CourseResponseDTO response = courseService.createCourse(javaRequest);

        assertCourse(response, "Java Development", "Java course", 20, List.of("John Smith"));
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_ShouldThrowConflict_WhenCourseAlreadyExists() {
        when(courseRepository.existsByCourseName("Java Development")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> courseService.createCourse(javaRequest)
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void getAllCourses_ShouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(javaCourse, cSharpCourse));

        List<CourseResponseDTO> response = courseService.getAllCourses();

        assertEquals(2, response.size());
        assertCourse(response.get(0), "Java Development", "Java course", 20, List.of("John Smith"));
        assertCourse(response.get(1), "C# Development", "C# course", 15, List.of("Sarah Jones"));
    }

    @Test
    void updateCourse_ShouldUpdateCourse_WhenCourseExists() {
        CourseRequestDTO updateRequest = request("Updated Java Course", "Updated description", 25, List.of(1));
        Course updatedCourse = course(1, "Updated Java Course", "Updated description", 25, List.of(john));

        when(courseRepository.findById(1)).thenReturn(Optional.of(javaCourse));
        when(trainerRepository.findAllById(List.of(1))).thenReturn(List.of(john));
        when(courseRepository.save(javaCourse)).thenReturn(updatedCourse);

        CourseResponseDTO response = courseService.updateCourse(1, updateRequest);

        assertCourse(response, "Updated Java Course", "Updated description", 25, List.of("John Smith"));
    }

    @Test
    void updateCourse_ShouldThrowNotFound_WhenCourseDoesNotExist() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> courseService.updateCourse(99, javaRequest)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void deleteCourse_ShouldDeleteCourse_WhenCourseExists() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(javaCourse));

        assertTrue(courseService.deleteCourse(1));

        verify(courseRepository).delete(javaCourse);
    }

    @Test
    void deleteCourse_ShouldReturnFalse_WhenCourseDoesNotExist() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        assertFalse(courseService.deleteCourse(99));

        verify(courseRepository, never()).delete(any());
    }

    @Test
    void enrollTrainee_ShouldEnrollTrainee_WhenValidCourseAndTrainee() {
        Trainee trainee = new Trainee();

        when(courseRepository.findById(1)).thenReturn(Optional.of(javaCourse));
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));

        CourseResponseDTO response = courseService.enrollTrainee(1, 1);

        assertCourse(response, "Java Development", "Java course", 20, List.of("John Smith"));
        assertEquals(javaCourse, trainee.getCourse());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void enrollTrainee_ShouldThrowNotFound_WhenCourseDoesNotExist() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> courseService.enrollTrainee(99, 1)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(traineeRepository, never()).findById(any());
    }

    @Test
    void enrollTrainee_ShouldThrowNotFound_WhenTraineeDoesNotExist() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(javaCourse));
        when(traineeRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> courseService.enrollTrainee(1, 99)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void enrollTrainee_ShouldThrowConflict_WhenTraineeAlreadyEnrolled() {
        Trainee trainee = new Trainee();
        trainee.setCourse(javaCourse);

        when(courseRepository.findById(1)).thenReturn(Optional.of(javaCourse));
        when(traineeRepository.findById(1)).thenReturn(Optional.of(trainee));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> courseService.enrollTrainee(1, 1)
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(traineeRepository, never()).save(any());
    }

    @Test
    void getTrainersCourses_ShouldReturnCourses_WhenTrainerExists() {
        john.setCourses(List.of(javaCourse, cSharpCourse));
        when(trainerRepository.findById(1)).thenReturn(Optional.of(john));

        List<CourseResponseDTO> response = courseService.getTrainersCourses(1);

        assertEquals(2, response.size());
    }

    @Test
    void getTrainersCourses_ShouldThrowNotFound_WhenTrainerDoesNotExist() {
        when(trainerRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> courseService.getTrainersCourses(99)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    private Trainer trainer(String firstName, String lastName) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        return trainer;
    }

    private Course course(Integer id, String name, String description, Integer maxStudents, List<Trainer> trainers) {
        Course course = new Course(
                name,
                description,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 7, 1),
                maxStudents,
                trainers
        );

        ReflectionTestUtils.setField(course, "courseId", id);
        return course;
    }

    private CourseRequestDTO request(String name, String description, Integer maxStudents, List<Integer> trainerIds) {
        CourseRequestDTO request = new CourseRequestDTO();
        request.setCourseName(name);
        request.setDescription(description);
        request.setStartDate(LocalDate.of(2026, 5, 1));
        request.setEndDate(LocalDate.of(2026, 7, 1));
        request.setMaxStudents(maxStudents);
        request.setTrainerIds(trainerIds);
        return request;
    }

    private void assertCourse(CourseResponseDTO response, String name, String description, int maxStudents, List<String> trainers) {
        assertNotNull(response);
        assertEquals(name, response.getCourseName());
        assertEquals(description, response.getDescription());
        assertEquals(maxStudents, response.getMaxStudents());
        assertEquals(trainers, response.getTrainerNames());
    }
}