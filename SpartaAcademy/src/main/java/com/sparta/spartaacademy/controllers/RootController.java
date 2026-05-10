package com.sparta.spartaacademy.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController
{
    @GetMapping("/")
    public ResponseEntity<Void> redirectToSwaggerUK(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/swagger-ui/index.html");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
