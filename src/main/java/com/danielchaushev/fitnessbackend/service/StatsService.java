package com.danielchaushev.fitnessbackend.service;

import com.danielchaushev.fitnessbackend.exception.InsufficientDataException;
import com.danielchaushev.fitnessbackend.model.User;
import com.danielchaushev.fitnessbackend.repository.BodyWeightLogRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final BodyWeightLogRepository bodyWeightLogRepository;

    public StatsService(BodyWeightLogRepository bodyWeightLogRepository) {
        this.bodyWeightLogRepository = bodyWeightLogRepository;
    }

    public Double getCurrentWeight(User user) {
        return bodyWeightLogRepository.findTopByUserIdOrderByDateDesc(user.getId())
                .map(log -> log.getWeightKg())
                .orElse(user.getWeight());
    }

    public double calculateBmi(User user) {
        Double weight = getCurrentWeight(user);
        validateBodyData(user, weight);
        double heightM = user.getHeight() / 100.0;
        return weight / (heightM * heightM);
    }

    public String getBmiCategory(double bmi) {
        if (bmi < 18.5) return "Поднормено тегло";
        if (bmi < 25) return "Нормално тегло";
        if (bmi < 30) return "Наднормено тегло";
        return "Затлъстяване";
    }

    private double getActivityMultiplier(String activityLevel) {
        if (activityLevel == null) return 1.375;

        return switch (activityLevel) {
            case "sedentary" -> 1.2;
            case "light" -> 1.375;
            case "moderate" -> 1.55;
            case "active" -> 1.725;
            case "very_active" -> 1.9;
            default -> 1.375;
        };
    }

    public double calculateDailyCalorieNeeds(User user) {
        Double weight = getCurrentWeight(user);
        validateBodyData(user, weight);

        if (user.getGender() == null || user.getGender().isBlank()) {
            throw new InsufficientDataException("Липсва пол в профила - нужен за изчисление на калорийните нужди.");
        }

        double base = (10 * weight) + (6.25 * user.getHeight()) - (5 * user.getAge());

        if ("male".equals(user.getGender())) {
            base += 5;
        } else {
            base -= 161;
        }

        double multiplier = getActivityMultiplier(user.getActivityLevel());
        return base * multiplier;
    }

    private void validateBodyData(User user, Double weight) {
        if (user.getHeight() == null || weight == null || user.getAge() == null) {
            throw new InsufficientDataException(
                    "Непълен профил - нужни са ръст, тегло и възраст за това изчисление."
            );
        }
    }
}