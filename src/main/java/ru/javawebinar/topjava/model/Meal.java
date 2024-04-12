package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private Integer userId;

    public Meal(LocalDateTime dateTime, String description, int calories, Integer userId) {
        this(null, dateTime, description, calories, userId);
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories, null);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories, Integer userId) {
        super(id);
        this.userId = userId;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
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

    public Integer getUserId() {
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
