package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.dtos.CourseRequestMapper;
import com.sparta.spartaacademy.dtos.CourseResponseMapper;
import com.sparta.spartaacademy.entities.Course;
import com.sparta.spartaacademy.repositories.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseRequestMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseRequestMapper courseMapper) {
        if (courseRepository == null || courseMapper == null) {
            throw new IllegalArgumentException("repository and mapper cannot be null");
        }
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }


    public boolean deleteCourse(int id) {
        return courseRepository.findById(id)
                .map(course -> {
                    courseRepository.delete(course);
                    return true;
                })
                .orElse(false);
    }


}
