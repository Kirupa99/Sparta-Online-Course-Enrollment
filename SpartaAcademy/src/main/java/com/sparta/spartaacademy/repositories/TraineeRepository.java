package com.sparta.spartaacademy.repositories;

import com.sparta.spartaacademy.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.spartaacademy.entities.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {

    List<Trainee> findByFirstNameContainingIgnoreCase(String firstName);

    List<Trainee> findByCourse_CourseId(Integer courseId);

    boolean existsByEmail(String email);

    Optional<Trainee> findByEmail(String email);

    Integer course(Course course);

    List<Trainee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourse_CourseNameContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            String courseName
    );
}
