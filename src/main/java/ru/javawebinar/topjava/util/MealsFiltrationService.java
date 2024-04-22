package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsFiltrationService {
    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return MealsFiltrationService.filterByPredicate(meals, caloriesPerDay, meal -> true, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay,
                                              LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        return MealsFiltrationService.filterByPredicate(meals, caloriesPerDay,
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime),
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate));
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filterTime, Predicate<Meal> filterDate) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filterDate.and(filterTime))
                .map(meal -> MealsFiltrationService.createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
