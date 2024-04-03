package ru.javawebinar.topjava.web;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static AtomicInteger authUserId = new AtomicInteger(1);

    public static void setUser1() {
        SecurityUtil.authUserId = new AtomicInteger(1);
    }

    public static void setUser2() {
        SecurityUtil.authUserId = new AtomicInteger(2);
    }

    public static int authUserId() {
        return SecurityUtil.authUserId.get();
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}