package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.exception.ResourceNotFoundException;
import com.danielchaushev.fitnessbackend.model.User;
import com.danielchaushev.fitnessbackend.repository.UserRepository;
import com.danielchaushev.fitnessbackend.service.NutritionService;
import com.danielchaushev.fitnessbackend.service.ProgressService;
import com.danielchaushev.fitnessbackend.service.StatsService;
import com.danielchaushev.fitnessbackend.service.WorkoutService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final UserRepository userRepository;
    private final NutritionService nutritionService;
    private final StatsService statsService;
    private final ProgressService progressService;
    private final WorkoutService workoutService;

    public StatsController(UserRepository userRepository, NutritionService nutritionService,
                           StatsService statsService, ProgressService progressService,
                           WorkoutService workoutService) {
        this.userRepository = userRepository;
        this.nutritionService = nutritionService;
        this.statsService = statsService;
        this.progressService = progressService;
        this.workoutService = workoutService;
    }

    @Getter
    @Setter
    public static class BmiResponse {
        private double bmi;
        private String category;
        private double dailyCalorieNeeds;
    }

    @GetMapping("/bmi/{userId}")
    public BmiResponse getBmi(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("Потребител с id " + userId + " не е намерен.");
        }

        BmiResponse response = new BmiResponse();
        double bmi = statsService.calculateBmi(user);
        response.setBmi(Math.round(bmi * 10) / 10.0);
        response.setCategory(statsService.getBmiCategory(bmi));
        response.setDailyCalorieNeeds(Math.round(statsService.calculateDailyCalorieNeeds(user)));

        return response;
    }

    @GetMapping("/nutrition-summary/{userId}")
    public NutritionService.DailySummary getNutritionSummary(
            @PathVariable Long userId,
            @RequestParam LocalDate date) {
        return nutritionService.calculateDailySummary(userId, date);
    }

    @GetMapping("/weight-trend/{userId}")
    public double getWeightTrend(@PathVariable Long userId) {
        return Math.round(progressService.calculateWeeklyTrend(userId) * 1000) / 1000.0;
    }

    @GetMapping("/workout-volume/{workoutId}")
    public double getWorkoutVolume(@PathVariable Long workoutId) {
        return workoutService.calculateTotalVolume(workoutId);
    }
}