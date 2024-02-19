package ru.javawebinar.topjava.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public enum MealInMemoryRepository implements MealRepository {
    INSTANCE;
    private final static Logger log = LoggerFactory.getLogger(MealInMemoryRepository.class);


    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);
    private final ReentrantLock createLock = new ReentrantLock();

    public List<Meal> getAll() {
        return this.meals.values().stream().map(Meal::new).collect(Collectors.toList());
    }

    @Override
    public Meal getById(int id) {
        return new Meal(this.meals.get(id));
    }

    @Override
    public void delete(int id) {
        this.meals.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        if (this.meals.containsKey(meal.getId()))
            return new Meal(Objects.requireNonNull(this.meals.put(meal.getId(), new Meal(meal))));
        else {
            log.warn("Meal with id {} is not present", meal.getId());
            return null;
        }
    }

    @Override
    public Meal create(Meal meal) {
        int id = this.currentId.incrementAndGet();
        createLock.lock();
        try {
            this.meals.put(id, new Meal(id, meal));
            return this.meals.get(id);
        } finally {
            createLock.unlock();
        }
    }

    @Override
    public Collection<Meal> addAll(Collection<Meal> meals) {
        return meals.stream().peek(this::create).collect(Collectors.toList());
    }
}
