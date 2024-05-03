package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

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

    public List<Meal> getAll(int userId) {
        return this.mealRepository.getAll(userId);
    }

    public List<Meal> getAllByFilter(int userId, LocalDate startDate, LocalDate endDate) {
        return this.mealRepository.getAllByFilter(userId, startDate, endDate);
    }

    public void update(Meal meal, int userId) {
        Meal updated = this.mealRepository.save(meal, userId);
        if (updated == null)
            throw new NotFoundException("Указанная еда отсутствует у данного пользователя");
        ValidationUtil.checkNotFoundWithId(updated, userId);
    }
}