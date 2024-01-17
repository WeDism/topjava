package ru.javawebinar.topjava.util;

import org.apache.commons.lang3.tuple.MutablePair;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
//        List<UserMealWithExcess> mealsTo = UserMealsUtil.filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);
        List<UserMealWithExcess> mealsTo = UserMealsUtil.filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        Map<LocalDate, MutablePair<Integer, List<UserMeal>>> localDateListMap = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            if (localDateListMap.containsKey(localDate)) {
                MutablePair<Integer, List<UserMeal>> integerListPair = localDateListMap.get(localDate);
                integerListPair.setLeft(integerListPair.getLeft() + meal.getCalories());
                integerListPair.getRight().add(meal);
            } else localDateListMap.put(localDate, MutablePair.of(meal.getCalories(), new ArrayList<>(List.of(meal))));
        }
        for (Map.Entry<LocalDate, MutablePair<Integer, List<UserMeal>>> localDateMutablePairEntry : localDateListMap.entrySet()) //хоть это и вложенный но по факту это просто упрощенное представление коневой коллекции в for
            for (UserMeal userMeal : localDateMutablePairEntry.getValue().getRight())
                if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                    userMealWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), caloriesPerDay < localDateMutablePairEntry.getValue().getLeft()));
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> localDateListMap = new HashMap<>();
        meals.forEach(e -> {
            if (localDateListMap.containsKey(e.getDateTime().toLocalDate()))
                localDateListMap.merge(e.getDateTime().toLocalDate(), e.getCalories(), Integer::sum);
            else localDateListMap.put(e.getDateTime().toLocalDate(), e.getCalories());
        });
        return meals.stream()
                .map(e -> new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), localDateListMap.get(e.getDateTime().toLocalDate()) > caloriesPerDay))
                .filter(e -> TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
    }
}
