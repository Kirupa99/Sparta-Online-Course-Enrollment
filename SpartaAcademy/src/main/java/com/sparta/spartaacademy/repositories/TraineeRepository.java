package com.sparta.spartaacademy.repositories;

import com.sparta.spartaacademy.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
}
