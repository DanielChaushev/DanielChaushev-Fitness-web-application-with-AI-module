package com.danielchaushev.fitnessbackend.repository;

import com.danielchaushev.fitnessbackend.model.AiPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AiPlanRepository extends JpaRepository<AiPlan, Long> {
    List<AiPlan> findByUserId(Long userId);
}