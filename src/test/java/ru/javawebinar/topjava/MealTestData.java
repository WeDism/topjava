package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MealTestData {
    public static final int MEAL_ID_BREAKFAST_1_USER = UserTestData.END_USER_SEQ;
    public static final int MEAL_ID_LUNCH_1_USER = UserTestData.END_USER_SEQ + 1;
    public static final int MEAL_ID_SNACK_1_USER = UserTestData.END_USER_SEQ + 2;
    public static final int MEAL_ID_DINNER_1_USER = UserTestData.END_USER_SEQ + 3;
    public static final int MEAL_ID_BREAKFAST_ADMIN = UserTestData.END_USER_SEQ + 4;
    public static final int MEAL_ID_LUNCH_2_USER = UserTestData.END_USER_SEQ + 5;
    public static final int MEAL_ID_NOT_EXISTING = UserTestData.END_USER_SEQ *2;

    public static final Meal breakfast1User = new Meal(MealTestData.MEAL_ID_BREAKFAST_1_USER,
            LocalDateTime.of(2024, 11, 1, 8, 0), "завтрак", 300);
    public static final Meal lunch1User = new Meal(MealTestData.MEAL_ID_LUNCH_1_USER,
            LocalDateTime.of(2024, 11, 1, 12, 15), "обед", 1200);
    public static final Meal snack1User = new Meal(MealTestData.MEAL_ID_SNACK_1_USER,
            LocalDateTime.of(2024, 11, 1, 15, 0), "перекус", 200);
    public static final Meal dinner1User = new Meal(MealTestData.MEAL_ID_DINNER_1_USER,
            LocalDateTime.of(2024, 11, 1, 17, 10), "ужин", 800);
    public static final Meal breakfastAdmin = new Meal(MealTestData.MEAL_ID_BREAKFAST_ADMIN,
            LocalDateTime.of(2024, 11, 2, 8, 0), "завтрак", 800);
    public static final Meal lunch2User = new Meal(MealTestData.MEAL_ID_LUNCH_2_USER,
            LocalDateTime.of(2024, 11, 2, 17, 0), "обед", 1500);
    public static final List<Meal> mealsUserFirstDay =
            Arrays.asList(MealTestData.breakfast1User, MealTestData.lunch1User, MealTestData.snack1User, MealTestData.dinner1User);
    public static final List<Meal> mealsUserAllDay =
            Arrays.asList(MealTestData.breakfast1User, MealTestData.lunch1User, MealTestData.snack1User, MealTestData.dinner1User, MealTestData.lunch2User);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2024, 11, 3, 14, 14), "Еда", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(lunch2User);
        updated.setCalories(900);
        updated.setDescription("завтрак");
        updated.setDateTime(LocalDateTime.of(2024, 11, 2, 9, 0));
        return updated;
    }

    public static Meal getDuplicateUpdated() {
        Meal updated = new Meal(lunch1User);
        updated.setCalories(900);
        updated.setDescription("завтрак");
        updated.setDateTime(LocalDateTime.of(2024, 11, 2, 9, 0));
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        Assertions.assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").containsExactlyInAnyOrderElementsOf(expected);
    }
}
