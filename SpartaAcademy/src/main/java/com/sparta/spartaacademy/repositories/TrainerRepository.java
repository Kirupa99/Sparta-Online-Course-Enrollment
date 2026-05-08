package com.sparta.spartaacademy.repositories;

import com.sparta.spartaacademy.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

    boolean existsByEmail(String email);
}
