package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = em.getReference(User.class, userId);
        meal.setUser(user);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            if (this.get(meal.getId(), userId) == null)
                return null;
            return em.merge(meal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    @Transactional
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createNamedQuery(Meal.BY_USER, Meal.class)
                .setParameter(1, id)
                .setParameter(2, userId)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
    }


    @Override
    @Transactional
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.BY_ALL_SORTED, Meal.class)
                .setParameter(1, userId)
                .getResultList();
    }

    @Override
    @Transactional
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BY_BETWEEN_HALF_OPEN, Meal.class)
                .setParameter(1, userId)
                .setParameter(2, startDateTime)
                .setParameter(3, endDateTime)
                .getResultList();
    }
}