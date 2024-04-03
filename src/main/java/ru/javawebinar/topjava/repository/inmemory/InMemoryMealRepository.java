package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealsUtil.meals1.forEach((meal -> this.save(meal, SecurityUtil.authUserId())));
        SecurityUtil.setUser2();
        MealsUtil.meals2.forEach((meal -> this.save(meal, SecurityUtil.authUserId())));
        SecurityUtil.setUser1();
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            int mealId = counter.incrementAndGet();
            repository.put(mealId, ((Meal) meal.setUserId(userId).setId(mealId)));
            return meal;
        }
        // handle case: update, but not present in storage
        if (meal.getUserId() != userId) throw new NumberFormatException("У данного поьзователя нет такой еды");
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return this.get(id, userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && userId == meal.getUserId() ? meal : null;
    }

    @Override
    public Collection<Meal> getAllByUser(int userId) {
        return repository.values().stream()
                .filter(meal -> userId == meal.getUserId())
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

