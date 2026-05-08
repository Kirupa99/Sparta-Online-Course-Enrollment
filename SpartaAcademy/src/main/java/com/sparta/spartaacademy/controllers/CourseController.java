package com.sparta.spartaacademy.controllers;

import com.sparta.spartaacademy.dtos.CourseRequestDTO;
import com.sparta.spartaacademy.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")

public class CourseController
{
    private final CourseService service;
    public CourseController(CourseService service)
    {
        this.service = service;
    }

    @Operation(summary = "Delete an existing Course", description = "Delete a course from the database")
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteCourse(@PathVariable int id)
    {
        boolean deletecourse = service.deleteCourse(id);
        if (deletecourse)
        {
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
