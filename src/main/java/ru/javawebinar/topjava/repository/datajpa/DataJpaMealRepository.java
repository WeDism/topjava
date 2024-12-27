package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository crudRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        User user = new User();
        user.setId(userId);
        meal.setUser(user);
        return (meal.isNew() || this.crudRepository.getMealByUserId(meal.getId(), userId).isPresent())
                ? this.crudRepository.save(meal)
                : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return this.crudRepository.deleteMealByUserId(id, userId) > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return this.crudRepository.getMealByUserId(id, userId).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return this.crudRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return this.crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}