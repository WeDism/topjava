package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.util.MealSampleData;

import java.util.concurrent.atomic.AtomicInteger;

public class SecurityUtil {
    private static final AtomicInteger authUserId = new AtomicInteger(1);

    public static void setUser(int userId) {
        SecurityUtil.authUserId.set(userId);
    }

    public static int authUserId() {
        return SecurityUtil.authUserId.get();
    }

    public static int authUserCaloriesPerDay() {
        return MealSampleData.DEFAULT_CALORIES_PER_DAY;
    }
}