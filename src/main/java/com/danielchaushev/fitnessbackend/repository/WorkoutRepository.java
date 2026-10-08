package com.danielchaushev.fitnessbackend.repository;

import com.danielchaushev.fitnessbackend.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long userId);
    List<Workout> findByUserIdOrderByDateDescIdDesc(Long userId);
}