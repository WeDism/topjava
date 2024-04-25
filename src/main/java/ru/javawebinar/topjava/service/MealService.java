package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MealService {
    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal create(Meal meal, int userId) {
        Meal saved = this.mealRepository.save(meal, userId);
        return ValidationUtil.checkNotFoundWithId(saved, saved.getId());
    }

    public void delete(int id, int userId) {
        ValidationUtil.checkNotFoundWithId(this.mealRepository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return ValidationUtil.checkNotFoundWithId(this.mealRepository.get(id, userId), id);
    }

    public List<Meal> getAll(int userId) {
        return this.mealRepository.getAll(userId);
    }

    public List<Meal> getAllBySorted(int userId,
                                     LocalTime startTime, LocalTime endTime,
                                     LocalDate startDate, LocalDate endDate, int caloriesPerDay) {
        return this.mealRepository.getAllFilteredEntity(userId, startTime, endTime, startDate, endDate, caloriesPerDay);
    }

    public void update(Meal meal, int userId) {
        ValidationUtil.checkNotFoundWithId(this.mealRepository.save(meal, userId), userId);
    }
}