package com.sparta.spartaacademy.repositories;

import com.sparta.spartaacademy.entities.Course;
import com.sparta.spartaacademy.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {

    List<Trainee> findByFirstNameContainingIgnoreCase(String firstName);

    List<Trainee> findByCourse_CourseId(Integer courseId);

    boolean existsByEmail(String email);

    Integer course(Course course);
}
