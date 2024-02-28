package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.Collection;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealService(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    public Meal create(Meal meal) {
        return mealRepository.save(meal);
    }

    public void delete(int id, int userId) {
        ValidationUtil.checkNotFoundWithId(mealRepository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return ValidationUtil.checkNotFoundWithId(mealRepository.get(id, userId), id);
    }

    public Collection<Meal> getAll() {
        return mealRepository.getAll();
    }

    public Collection<Meal> getAllByUser(int userId) {
        return mealRepository.getAllByUser(userId);
    }

    public void update(Meal meal, int userId) {
        ValidationUtil.checkNotFoundWithId(mealRepository.save(meal), userId);
    }

}