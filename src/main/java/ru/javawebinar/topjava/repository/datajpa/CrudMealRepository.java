package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Modifying
    @Transactional
    @Query("delete Meal m where m.id=:mealId and m.user.id=:userId")
    int deleteMealByUserId(@Param("mealId") int mealId, @Param("userId") int userId);

    @Query("select m from Meal m where m.id=:mealId and m.user.id=:userId")
    Meal getMealByUserId(@Param("mealId") int mealId, @Param("userId") int userId);

    @Query("select m from Meal m join fetch m.user u where m.id=:mealId and u.id=:userId")
    Meal getMealWithUser(@Param("mealId") int mealId, @Param("userId") int userId);

    @Query("select m from Meal m where m.dateTime>=:startDateTime and m.dateTime<:endDateTime and m.user.id=:userId order by m.dateTime desc")
    List<Meal> getBetweenHalfOpen(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("userId") int userId);

    // @Query("select m from Meal m where m.user.id=:userId order by m.dateTime desc")
    List<Meal> findByUserOrderByDateTimeDesc(@Param("user") User user);
}
