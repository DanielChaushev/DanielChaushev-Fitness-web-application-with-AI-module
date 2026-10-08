package com.danielchaushev.fitnessbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private Integer age;
    private Double height;
    private Double weight;
    private String goal;
    private String activityLevel;
}