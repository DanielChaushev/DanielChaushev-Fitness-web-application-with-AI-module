package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.dto.AiPlanRequest;
import com.danielchaushev.fitnessbackend.exception.ResourceNotFoundException;
import com.danielchaushev.fitnessbackend.model.AiPlan;
import com.danielchaushev.fitnessbackend.model.User;
import com.danielchaushev.fitnessbackend.repository.AiPlanRepository;
import com.danielchaushev.fitnessbackend.repository.UserRepository;
import com.danielchaushev.fitnessbackend.service.GeminiService;
import com.danielchaushev.fitnessbackend.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiPlanController {

    private final GeminiService geminiService;
    private final UserRepository userRepository;
    private final AiPlanRepository aiPlanRepository;
    private final StatsService statsService;

    public AiPlanController(GeminiService geminiService, UserRepository userRepository,
                            AiPlanRepository aiPlanRepository, StatsService statsService) {
        this.geminiService = geminiService;
        this.userRepository = userRepository;
        this.aiPlanRepository = aiPlanRepository;
        this.statsService = statsService;
    }

    @PostMapping("/generate/{userId}")
    public AiPlan generatePlan(@PathVariable Long userId, @RequestBody AiPlanRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Потребител с id " + userId + " не е намерен."));

        double calorieNeeds = statsService.calculateDailyCalorieNeeds(user);
        Double currentWeight = statsService.getCurrentWeight(user);

        String prompt = String.format(
                "Направи комбиниран фитнес и хранителен план за човек със следните данни: " +
                        "възраст %d години, тегло %.1f кг, ръст %.1f см. Цел: %s. " +
                        "Базовите дневни калорийни нужди за поддържане на тегло са около %.0f ккал - коригирай ги спрямо целта " +
                        "(лек излишък при покачване на маса, лек дефицит при отслабване). " +
                        "Планът трябва да съдържа две ясно разделени части: " +
                        "1) Тренировъчна програма за точно %d дни в седмицата, с конкретни упражнения по дни, брой серии и повторения и реалистични времена за почивка. " +
                        "2) Дневен хранителен план - закуска, обяд, вечеря и снаксове при нужда, с конкретни храни и грамажи, обща дневна калорийност и разбивка на белтъчини, въглехидрати и мазнини в грамове. " +
                        "Форматирай в Markdown: ### за заглавие на всяка част, #### за всеки ден (напр. '#### Ден 1: Бутащи упражнения'), всяко упражнение на отделен ред в списък с удебелено име. " +
                        "Без емоджита и символи пред заглавията. Не слагай мускулната група в скоби след името на упражнението. " +
                        "Отговори изцяло на български, с разговорните имена на упражненията от българските фитнес зали " +
                        "(напр. 'Лежанка с лост', не 'Лег на права пейка') и без буквално преведени английски думи - " +
                        "'серии', не 'сетове'; 'Част 1', не 'Парт 1'; 'загрявка', не 'загряване'.",
                user.getAge(), currentWeight, user.getHeight(), user.getGoal(), calorieNeeds,
                request.getDaysPerWeek()
        );

        String generatedText = geminiService.generateContent(prompt);

        AiPlan plan = new AiPlan();
        plan.setUser(user);
        plan.setContent(generatedText);
        plan.setGeneratedAt(LocalDateTime.now());
        return aiPlanRepository.save(plan);
    }

    @GetMapping("/user/{userId}")
    public List<AiPlan> getPlansByUser(@PathVariable Long userId) {
        return aiPlanRepository.findByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        if (!aiPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("План с id " + id + " не е намерен.");
        }
        aiPlanRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}