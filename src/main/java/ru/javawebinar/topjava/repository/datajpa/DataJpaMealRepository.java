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
    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = this.userRepository.getReferenceById(userId);
        meal.setUser(user);
        return (meal.isNew() || Objects.nonNull(this.crudRepository.getMealByUserId(meal.getId(), userId)))
                ? this.crudRepository.save(meal)
                : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return this.crudRepository.deleteMealByUserId(id, userId) > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return this.crudRepository.getMealByUserId(id, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return this.crudRepository.getMealWithUser(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return this.crudRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return this.crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
