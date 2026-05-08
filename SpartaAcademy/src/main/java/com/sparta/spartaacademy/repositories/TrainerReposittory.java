package com.sparta.spartaacademy.repositories;

import com.sparta.spartaacademy.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerReposittory extends JpaRepository<Trainer, Integer> {

    public List<Trainer> findTrainersByTrainerId(List<Integer> trainerIds);
}
