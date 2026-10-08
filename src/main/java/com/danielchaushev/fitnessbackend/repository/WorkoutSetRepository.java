package com.danielchaushev.fitnessbackend.repository;

import com.danielchaushev.fitnessbackend.model.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    List<WorkoutSet> findByWorkoutId(Long workoutId);
}