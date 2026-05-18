package com.sparta.spartaacademy.repositories;

import com.sparta.spartaacademy.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    public boolean existsByCourseName(String courseName);

    List<Course> findCoursesByCourseNameContainingIgnoreCase(String courseName);

    List<Course> findByTrainers_Email(String email);
}

