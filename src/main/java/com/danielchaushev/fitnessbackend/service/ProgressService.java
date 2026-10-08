package com.danielchaushev.fitnessbackend.service;

import com.danielchaushev.fitnessbackend.exception.InsufficientDataException;
import com.danielchaushev.fitnessbackend.model.BodyWeightLog;
import com.danielchaushev.fitnessbackend.repository.BodyWeightLogRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ProgressService {

    private final BodyWeightLogRepository bodyWeightLogRepository;

    public ProgressService(BodyWeightLogRepository bodyWeightLogRepository) {
        this.bodyWeightLogRepository = bodyWeightLogRepository;
    }

    public double calculateWeeklyTrend(Long userId) {
        List<BodyWeightLog> logs = bodyWeightLogRepository.findByUserIdOrderByDateAsc(userId);

        if (logs.size() < 2) {
            throw new InsufficientDataException(
                    "Нужни са поне 2 записа на тегло, за да се извърши изчислението. Налични записи в момента: " + logs.size()
            );
        }

        BodyWeightLog first = logs.get(0);
        BodyWeightLog last = logs.get(logs.size() - 1);

        double weightChange = last.getWeightKg() - first.getWeightKg();
        long daysBetween = ChronoUnit.DAYS.between(first.getDate(), last.getDate());

        if (daysBetween == 0) {
            throw new InsufficientDataException("Записите на тегло трябва да са от различни дати.");
        }

        double weeksBetween = daysBetween / 7.0;
        return weightChange / weeksBetween;
    }
}