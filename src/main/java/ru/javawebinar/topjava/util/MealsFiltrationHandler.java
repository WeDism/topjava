package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsFiltrationHandler {
    public static List<Meal> getFilteredEntity(Collection<Meal> meals,
                                                   LocalDate startDate,
                                                   LocalDate endDate,
                                                   Comparator<Meal> comparator) {
        Predicate<Meal> filterDate = meal -> DateTimeComparator.isBetweenClosed(meal.getDate(), startDate, endDate);
        return meals.stream()
                .filter(filterDate)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    static <T> List<T> filterByPredicate(Collection<Meal> meals,
                                         int caloriesPerDay,
                                         Predicate<Meal> filterTime,
                                         Predicate<Meal> filterDate,
                                         BiFunction<Meal, Boolean, T> createFunction) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return meals.stream()
                .filter(filterDate.and(filterTime))
                .map(meal -> createFunction.apply(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
