package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.exception.ResourceNotFoundException;
import com.danielchaushev.fitnessbackend.model.Workout;
import com.danielchaushev.fitnessbackend.repository.WorkoutRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutRepository workoutRepository;

    public WorkoutController(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @GetMapping("/user/{userId}")
    public List<Workout> getWorkoutsByUser(@PathVariable Long userId) {
        return workoutRepository.findByUserIdOrderByDateDescIdDesc(userId);
    }

    @GetMapping("/{id}")
    public Workout getWorkoutById(@PathVariable Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тренировка с id " + id + " не е намерена."));
    }

    @PostMapping
    public Workout createWorkout(@RequestBody Workout workout) {
        return workoutRepository.save(workout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new ResourceNotFoundException("Тренировка с id " + id + " не е намерена.");
        }
        workoutRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}