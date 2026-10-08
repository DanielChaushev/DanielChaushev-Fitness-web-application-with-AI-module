package com.danielchaushev.fitnessbackend.repository;

import com.danielchaushev.fitnessbackend.model.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findByUserIdAndDate(Long userId, LocalDate date);
}