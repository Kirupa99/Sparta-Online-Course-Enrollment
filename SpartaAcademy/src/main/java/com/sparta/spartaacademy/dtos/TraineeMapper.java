package com.sparta.spartaacademy.dtos;

import com.sparta.spartaacademy.entities.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    @Mapping(target = "traineeId", ignore = true)
    @Mapping(target = "course", ignore = true)
    Trainee toEntity(TraineeRequestDTO dto);

    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.courseName", target = "courseName")
    TraineeResponseDTO toResponseDTO(Trainee trainee);

    @Mapping(target = "traineeId", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateEntity(TraineeRequestDTO dto, @MappingTarget Trainee trainee);


}
