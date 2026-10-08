package com.danielchaushev.fitnessbackend.service;

import com.danielchaushev.fitnessbackend.model.WorkoutSet;
import com.danielchaushev.fitnessbackend.repository.WorkoutSetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutSetRepository workoutSetRepository;

    public WorkoutService(WorkoutSetRepository workoutSetRepository) {
        this.workoutSetRepository = workoutSetRepository;
    }

    public double calculateTotalVolume(Long workoutId) {
        List<WorkoutSet> sets = workoutSetRepository.findByWorkoutId(workoutId);

        double totalVolume = 0.0;
        for (WorkoutSet set : sets) {
            totalVolume += set.getReps() * set.getWeightKg();
        }
        return totalVolume;
    }
}