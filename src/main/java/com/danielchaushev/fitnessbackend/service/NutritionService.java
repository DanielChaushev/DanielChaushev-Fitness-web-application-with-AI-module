package com.danielchaushev.fitnessbackend.service;

import com.danielchaushev.fitnessbackend.model.MealLog;
import com.danielchaushev.fitnessbackend.repository.MealLogRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NutritionService {

    private final MealLogRepository mealLogRepository;

    public NutritionService(MealLogRepository mealLogRepository) {
        this.mealLogRepository = mealLogRepository;
    }

    @Getter
    @Setter
    public static class DailySummary {
        private double totalCalories;
        private double totalProtein;
        private double totalCarbs;
        private double totalFat;
    }

    public DailySummary calculateDailySummary(Long userId, LocalDate date) {
        List<MealLog> logs = mealLogRepository.findByUserIdAndDate(userId, date);

        DailySummary summary = new DailySummary();
        for (MealLog log : logs) {
            double ratio = log.getGrams() / 100.0;
            summary.setTotalCalories(summary.getTotalCalories() + log.getFood().getCaloriesPer100g() * ratio);
            summary.setTotalProtein(summary.getTotalProtein() + log.getFood().getProteinPer100g() * ratio);
            summary.setTotalCarbs(summary.getTotalCarbs() + log.getFood().getCarbsPer100g() * ratio);
            summary.setTotalFat(summary.getTotalFat() + log.getFood().getFatPer100g() * ratio);
        }
        return summary;
    }
}