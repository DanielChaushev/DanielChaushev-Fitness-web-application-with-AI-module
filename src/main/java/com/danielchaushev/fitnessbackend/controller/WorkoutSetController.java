package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.exception.ResourceNotFoundException;
import com.danielchaushev.fitnessbackend.model.WorkoutSet;
import com.danielchaushev.fitnessbackend.repository.WorkoutSetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-sets")
public class WorkoutSetController {

    private final WorkoutSetRepository workoutSetRepository;

    public WorkoutSetController(WorkoutSetRepository workoutSetRepository) {
        this.workoutSetRepository = workoutSetRepository;
    }

    @GetMapping("/workout/{workoutId}")
    public List<WorkoutSet> getSetsByWorkout(@PathVariable Long workoutId) {
        return workoutSetRepository.findByWorkoutId(workoutId);
    }

    @PostMapping
    public WorkoutSet createSet(@RequestBody WorkoutSet workoutSet) {
        return workoutSetRepository.save(workoutSet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSet(@PathVariable Long id) {
        if (!workoutSetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Серия с id " + id + " не е намерена.");
        }
        workoutSetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}