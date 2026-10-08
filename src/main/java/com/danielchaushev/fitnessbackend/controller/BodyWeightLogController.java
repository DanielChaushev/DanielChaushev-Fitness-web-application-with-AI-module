package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.exception.ResourceNotFoundException;
import com.danielchaushev.fitnessbackend.model.BodyWeightLog;
import com.danielchaushev.fitnessbackend.repository.BodyWeightLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weight-logs")
public class BodyWeightLogController {

    private final BodyWeightLogRepository repository;

    public BodyWeightLogController(BodyWeightLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/user/{userId}")
    public List<BodyWeightLog> getLogsByUser(@PathVariable Long userId) {
        return repository.findByUserIdOrderByDateAsc(userId);
    }

    @PostMapping
    public BodyWeightLog createLog(@RequestBody BodyWeightLog log) {
        return repository.save(log);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Запис на тегло с id " + id + " не е намерен.");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}