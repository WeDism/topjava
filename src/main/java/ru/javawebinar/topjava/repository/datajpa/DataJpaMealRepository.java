package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository mealRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.mealRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = this.userRepository.getReferenceById(userId);
        meal.setUser(user);
        return (meal.isNew() || Objects.nonNull(this.mealRepository.getMealByUserId(meal.getId(), userId)))
                ? this.mealRepository.save(meal)
                : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return this.mealRepository.deleteMealByUserId(id, userId) > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return this.mealRepository.getMealByUserId(id, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return this.mealRepository.getMealWithUser(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        User referenceById = this.userRepository.getReferenceById(userId);
        return this.mealRepository.findByUserOrderByDateTimeDesc(referenceById);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return this.mealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
