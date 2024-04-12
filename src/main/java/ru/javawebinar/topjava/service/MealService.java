package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

@Service
public class MealService {
    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal create(Meal meal, int userId) {
        return this.mealRepository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        ValidationUtil.checkNotFoundWithId(this.mealRepository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return ValidationUtil.checkNotFoundWithId(this.mealRepository.get(id, userId), id);
    }

    public Collection<Meal> getAllByUser(int userId) {
        return this.mealRepository.getAllByUser(userId);
    }

    public Collection<Meal> getAllByUserWithFilter(int userId,
                                                   LocalTime startTime, LocalTime endTime,
                                                   LocalDate startDate, LocalDate endDate) {
        return this.mealRepository.getAllByUserWithFilter(userId, startTime, endTime, startDate, endDate);
    }

    public void update(Meal meal, int userId) {
        ValidationUtil.checkNotFoundWithId(this.mealRepository.save(meal, userId), userId);
    }
}