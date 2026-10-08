package com.danielchaushev.fitnessbackend.repository;

import com.danielchaushev.fitnessbackend.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}