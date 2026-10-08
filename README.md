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

### **Database:**
- MySQL (Relational database with optimized schema for users, workouts, logs, and AI plans)

### **Frontend:**
- HTML5 & CSS3
- Vanilla JavaScript
- Chart.js (for progress charts)

### **External APIs:**
- Google Gemini API

---

## ⚙️ Installation & Running Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/DanielChaushev/Fitness-web-application-with-AI-module.git
   ```

2. **Configure the Database:**
   - Create a MySQL database.
   - Update your `src/main/resources/application.properties` file with your database credentials:

     Properties
     ```ini
     spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. **Build and Run the Application:**

   Bash
   ```
   mvn spring-boot:run
   ```

4. **Access the App:** Open your browser and navigate to `http://localhost:8080`.
