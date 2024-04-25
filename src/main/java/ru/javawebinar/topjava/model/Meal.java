package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final Boolean excess;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories, null);
    }

    public Meal(LocalDateTime dateTime, String description, int calories, Boolean excess) {
        this(null, dateTime, description, calories, excess);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories, Boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = null;
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

    public Boolean getExcess() {
        return this.excess;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + super.id +
                ", dateTime=" + this.dateTime +
                ", description='" + this.description + '\'' +
                ", calories=" + this.calories +
                '}';
    }
}
