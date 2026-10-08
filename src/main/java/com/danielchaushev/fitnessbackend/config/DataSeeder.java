package com.danielchaushev.fitnessbackend.config;

import com.danielchaushev.fitnessbackend.model.Exercise;
import com.danielchaushev.fitnessbackend.model.Food;
import com.danielchaushev.fitnessbackend.repository.ExerciseRepository;
import com.danielchaushev.fitnessbackend.repository.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FoodRepository foodRepository;
    private final ExerciseRepository exerciseRepository;

    public DataSeeder(FoodRepository foodRepository, ExerciseRepository exerciseRepository) {
        this.foodRepository = foodRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) {
        seedFoods();
        seedExercises();
    }

    private void seedFoods() {
        if (foodRepository.count() > 0) return;

        addFood("Пилешко филе", 165, 31, 0, 3.6);
        addFood("Телешко месо", 250, 26, 0, 15);
        addFood("Свинско месо", 242, 27, 0, 14);
        addFood("Риба тон (консерва)", 132, 28, 0, 1);
        addFood("Сьомга", 208, 20, 0, 13);
        addFood("Яйца", 155, 13, 1.1, 11);
        addFood("Кашкавал", 350, 25, 2, 27);
        addFood("Извара", 98, 11, 3.4, 4.3);
        addFood("Кисело мляко", 61, 3.5, 4.7, 3.3);
        addFood("Мляко", 42, 3.4, 5, 1);
        addFood("Ориз (варен)", 130, 2.7, 28, 0.3);
        addFood("Овесени ядки", 389, 17, 66, 7);
        addFood("Хляб типов", 265, 9, 49, 3.2);
        addFood("Картофи (варени)", 87, 2, 20, 0.1);
        addFood("Леща (варена)", 116, 9, 20, 0.4);
        addFood("Боб (варен)", 127, 9, 23, 0.5);
        addFood("Банан", 89, 1.1, 23, 0.3);
        addFood("Ябълка", 52, 0.3, 14, 0.2);
        addFood("Домат", 18, 0.9, 3.9, 0.2);
        addFood("Краставица", 15, 0.7, 3.6, 0.1);
        addFood("Спанак", 23, 2.9, 3.6, 0.4);
        addFood("Броколи", 34, 2.8, 7, 0.4);
        addFood("Фъстъчено масло", 588, 25, 20, 50);
        addFood("Мед", 304, 0.3, 82, 0);
        addFood("Зехтин", 884, 0, 0, 100);
        addFood("Бадеми", 579, 21, 22, 50);
    }

    private void seedExercises() {
        if (exerciseRepository.count() > 0) return;

        addExercise("Клек с лост", "Крака");
        addExercise("Български клек", "Крака");
        addExercise("Лег преса", "Крака");
        addExercise("Разгъване за квадрицепс", "Крака");
        addExercise("Сгъване за задно бедро", "Крака");
        addExercise("Румънска тяга", "Крака");
        addExercise("Повдигане на пръсти", "Крака");
        addExercise("Лежанка", "Гърди");
        addExercise("Наклонена лежанка с дъмбели", "Гърди");
        addExercise("Флайс с дъмбели", "Гърди");
        addExercise("Лицеви опори", "Гърди");
        addExercise("Гребане с лост", "Гръб");
        addExercise("Гребане на скрипец", "Гръб");
        addExercise("Набирания", "Гръб");
        addExercise("Вертикален скрипец", "Гръб");
        addExercise("Мъртва тяга", "Гръб");
        addExercise("Военна преса", "Рамене");
        addExercise("Странично разтваряне с дъмбели", "Рамене");
        addExercise("Раменна преса с дъмбели", "Рамене");
        addExercise("Сгъване с лост за бицепс", "Бицепс");
        addExercise("Чуково сгъване", "Бицепс");
        addExercise("Трицепсово разгъване на скрипец", "Трицепс");
        addExercise("Кофички на пейка", "Трицепс");
        addExercise("Коремни преси", "Корем");
        addExercise("Планк", "Корем");
    }

    private void addFood(String name, double calories, double protein, double carbs, double fat) {
        Food food = new Food();
        food.setName(name);
        food.setCaloriesPer100g(calories);
        food.setProteinPer100g(protein);
        food.setCarbsPer100g(carbs);
        food.setFatPer100g(fat);
        foodRepository.save(food);
    }

    private void addExercise(String name, String muscleGroup) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setMuscleGroup(muscleGroup);
        exerciseRepository.save(exercise);
    }
}