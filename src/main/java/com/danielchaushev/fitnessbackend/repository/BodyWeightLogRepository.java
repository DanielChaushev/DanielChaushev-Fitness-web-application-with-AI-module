package com.danielchaushev.fitnessbackend.repository;

import com.danielchaushev.fitnessbackend.model.BodyWeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BodyWeightLogRepository extends JpaRepository<BodyWeightLog, Long> {
    List<BodyWeightLog> findByUserIdOrderByDateAsc(Long userId);
    Optional<BodyWeightLog> findTopByUserIdOrderByDateDesc(Long userId);
}