package com.sparta.spartaacademy.services;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.dtos.CourseResponseDTO;
import com.sparta.spartaacademy.entities.Course;
import com.sparta.spartaacademy.entities.Trainee;
import com.sparta.spartaacademy.entities.Trainer;
import com.sparta.spartaacademy.repositories.CourseRepository;
import com.sparta.spartaacademy.repositories.TraineeRepository;
import com.sparta.spartaacademy.repositories.TrainerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService{

    private final CourseRepository courseRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;

    public CourseService(CourseRepository courseRepository, TrainerRepository trainerRepository, TraineeRepository traineeRepository)
    {
        this.courseRepository = courseRepository;
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
      
    }
 
    public CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO){

        if(courseRepository.existsByCourseName(courseRequestDTO.getCourseName())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course with this name already exists");
        }

        Course course = mapToEntity(courseRequestDTO);
        Course savedCourse = courseRepository.save(course);
        CourseResponseDTO responseDTO = mapToResponse(savedCourse);

        return responseDTO;
    }

    private Course mapToEntity(CourseRequestDTO request){

        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setMaxStudents(request.getMaxStudents());
        if(request.getTrainerIds() != null){
            List<Trainer> trainers = trainerRepository.findAllById(request.getTrainerIds());
            course.setTrainers(trainers);
        }

        return course;
    }

    private CourseResponseDTO mapToResponse(Course course){
        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setCourseId(course.getCourseId());
        responseDTO.setCourseName(course.getCourseName());
        responseDTO.setDescription(course.getDescription());
        responseDTO.setStartDate(course.getStartDate());
        responseDTO.setEndDate(course.getEndDate());
        responseDTO.setMaxStudents(course.getMaxStudents());

        if(course.getTrainers() != null){
            responseDTO.setTrainerNames(course.getTrainers()
                    .stream().map(t -> t.getFirstName() + " " + t.getLastName()).collect(Collectors.toList()));
        }

        if(course.getTrainees() != null){
            responseDTO.setTraineeCount(course.getTrainees().size());
        }else{
            responseDTO.setTraineeCount(0);
        }

        return responseDTO;
    }


    public List<CourseResponseDTO> getAllCourses(){

        List<Course> allCourses =  courseRepository.findAll();
        return allCourses.stream().map(this::mapToResponse).collect(Collectors.toList());

    }

    public CourseResponseDTO getCourseById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Course not found with id: " + id));
        return mapToResponse(course);
    }

    public List<CourseResponseDTO> searchCoursesByName(String courseName){

        List<Course> courses = courseRepository.findCoursesByCourseNameContainingIgnoreCase(courseName);
        return courses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public CourseResponseDTO updateCourse(Integer courseId, CourseRequestDTO requestDTO){

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with Id: "+ courseId + " not found."));
        course.setCourseName(requestDTO.getCourseName());
        course.setDescription(requestDTO.getDescription());
        course.setStartDate(requestDTO.getStartDate());
        course.setEndDate(requestDTO.getEndDate());
        course.setMaxStudents(requestDTO.getMaxStudents());

        if(requestDTO.getTrainerIds() != null){
            List<Trainer> trainers = trainerRepository.findAllById(requestDTO.getTrainerIds());
            course.setTrainers(trainers);
        }

        Course updatedCourse = courseRepository.save(course);
        return mapToResponse(updatedCourse);
    }
  
  public boolean deleteCourse(int id) {
        return courseRepository.findById(id)
                .map(course -> {
                    courseRepository.delete(course);
                    return true;
                })
                .orElse(false);
    }


    public CourseResponseDTO enrollTrainee(Integer courseId, Integer traineeId){

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with courseId : "+courseId+" not found"));

        Trainee trainee = traineeRepository.findById(traineeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee with traineeId : "+ traineeId+ " not found"));

        if(trainee.getCourse() != null && trainee.getCourse().getCourseId().equals(courseId)){
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "Trainee is already enrolled in this course");
        }

        trainee.setCourse(course);
        traineeRepository.save(trainee);

        return mapToResponse(course);
    }


    public List<CourseResponseDTO> getTrainersCourses(Integer trainerId){

        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer with trainerId : "+ trainerId + " not found"));

        return trainer.getCourses().stream().map(this::mapToResponse).collect(Collectors.toList());
    }
}
