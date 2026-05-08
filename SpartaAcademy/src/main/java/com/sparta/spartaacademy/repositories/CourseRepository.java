package com.sparta.spartaacademy.repositories;


import com.sparta.spartaacademy.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer>
{
}
