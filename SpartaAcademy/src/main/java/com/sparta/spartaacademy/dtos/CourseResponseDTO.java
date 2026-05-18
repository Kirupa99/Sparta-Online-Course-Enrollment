package com.sparta.spartaacademy.dtos;

import java.time.LocalDate;
import java.util.List;

public class CourseResponseDTO {

    private Integer courseId;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxStudents;
    private List<String> trainerNames;
    private Integer traineeCount;
    private List<String> traineeNames;

    public CourseResponseDTO() {}

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }

    public List<String> getTrainerNames() { return trainerNames; }
    public void setTrainerNames(List<String> trainerNames) { this.trainerNames = trainerNames; }

    public Integer getTraineeCount() { return traineeCount; }
    public void setTraineeCount(Integer traineeCount) { this.traineeCount = traineeCount; }

    public List<String> getTraineeNames() { return traineeNames; }
    public void setTraineeNames(List<String> traineeNames) { this.traineeNames = traineeNames; }
}
