package ru.javawebinar.topjava.to;

import java.time.LocalDateTime;

public class MealTo {
    private final Integer id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public Integer getId() {
        return this.id;
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

    public boolean isExcess() {
        return this.excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + this.id +
                ", dateTime=" + this.dateTime +
                ", description='" + this.description + '\'' +
                ", calories=" + this.calories +
                ", excess=" + this.excess +
                '}';
    }
}
