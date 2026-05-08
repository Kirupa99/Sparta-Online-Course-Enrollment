package com.sparta.spartaacademy.dtos;

import com.sparta.spartaacademy.entities.Course;

public interface CourseResponseMapper
{
    CourseResponseDTO toResponseDTO(Course course);
}
