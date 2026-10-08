package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.exception.ResourceNotFoundException;
import com.danielchaushev.fitnessbackend.model.MealLog;
import com.danielchaushev.fitnessbackend.repository.MealLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meal-logs")
public class MealLogController {

    private final MealLogRepository mealLogRepository;

    public MealLogController(MealLogRepository mealLogRepository) {
        this.mealLogRepository = mealLogRepository;
    }

    @GetMapping("/user/{userId}")
    public List<MealLog> getLogsByUserAndDate(
            @PathVariable Long userId,
            @RequestParam LocalDate date) {
        return mealLogRepository.findByUserIdAndDate(userId, date);
    }

    @PostMapping
    public MealLog createMealLog(@RequestBody MealLog mealLog) {
        return mealLogRepository.save(mealLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealLog(@PathVariable Long id) {
        if (!mealLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Запис на хранене с id " + id + " не е намерен.");
        }
        mealLogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}