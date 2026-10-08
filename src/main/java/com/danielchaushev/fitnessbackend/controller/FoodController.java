package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.model.Food;
import com.danielchaushev.fitnessbackend.repository.FoodRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodRepository foodRepository;

    public FoodController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @GetMapping
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    @GetMapping("/search")
    public List<Food> searchFoods(@RequestParam String name) {
        return foodRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    public Food createFood(@RequestBody Food food) {
        return foodRepository.save(food);
    }
}