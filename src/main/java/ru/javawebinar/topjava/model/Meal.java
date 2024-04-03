package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private Integer userId;

    public Meal(Integer userId, LocalDateTime dateTime, String description, int calories) {
        this(null, userId, dateTime, description, calories);
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, null, dateTime, description, calories);
    }

    public Meal(Integer id, Integer userId, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.userId = userId;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Meal(Meal meal, Integer userId) {
        this(meal.id, userId, meal.dateTime, meal.description, meal.calories);
    }

    public Meal(Meal meal, Integer mealId, Integer userId) {
        this(mealId, userId, meal.dateTime, meal.description, meal.calories);
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCalories() {
        return this.calories;
    }

    public LocalDate getDate() {
        return this.dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return this.dateTime.toLocalTime();
    }

    public int getUserId() {
        return this.userId;
    }

    public Meal setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + super.id +
                ", userId=" + this.userId +
                ", dateTime=" + this.dateTime +
                ", description='" + this.description + '\'' +
                ", calories=" + this.calories +
                '}';
    }
}
