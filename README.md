# Fitness Web Application with AI Module

A comprehensive full-stack web application developed as a bachelor's thesis project, designed for strength training tracking, daily nutrition monitoring, progress visualization, and AI-powered fitness planning.

---

## 🚀 Key Features

- **Workout Tracker:** Log and manage exercises, sets, weights, and automatically calculate training volume.
- **Nutrition & Macro Monitoring:** Calculate daily caloric needs using the *Mifflin-St Jeor* equation, track macro-nutrients (protein, carbs, fats), and log daily food intake.
- **AI Integration:** Powered by the Google Gemini API to automatically generate custom workout routines and diet plans tailored to individual user parameters.
- **Progress Visualization:** Interactive charts (powered by Chart.js) to track body weight changes and performance metrics over time.
- **Secure Authentication:** User management secured with Spring Security, BCrypt password hashing, and JWT-based authentication.

---

## 🛠️ Tech Stack

### **Backend:**
- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security & JWT
- Gradle

### **Database:**
- MySQL (Relational database with optimized schema for users, workouts, logs, and AI plans)

### **Frontend:**
- HTML5 & CSS3
- Vanilla JavaScript
- Chart.js (for progress charts)

### **External APIs:**
- Google Gemini API

---

## 📁 Project Structure

- `src/` – Spring Boot backend
- `frontend/` – HTML, CSS and JavaScript client

---

## ⚙️ Installation & Running Locally

**Requirements:** Java 21, MySQL

1. **Clone the repository:**
```bash
   git clone https://github.com/DanielChaushev/DanielChaushev-Fitness-web-application-with-AI-module.git
```

2. **Configure the Database:**
   - Create a MySQL database named `fitness_app`.
   - Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties`.
   - Fill in your MySQL username and password and your Google Gemini API key.

3. **Run the backend:**
```bash
   ./gradlew bootRun
```
   On Windows use `gradlew.bat bootRun`. The API runs on `http://localhost:8080`.

4. **Run the frontend:** open the `frontend/` folder in VS Code and start `frontend/html/index.html` with the **Live Server** extension.
