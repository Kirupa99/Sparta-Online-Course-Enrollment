package com.sparta.spartaacademy.dtos;

import com.sparta.spartaacademy.entities.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseRequestMapper

{
    Course toEntity(CourseRequestDTO dtoreq);
}
