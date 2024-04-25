package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeComparator {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T> boolean isBetweenHalfOpen(Comparable<T> comparable, T moreOrEqualObject, T lessObject) {
        return comparable.compareTo(moreOrEqualObject) >= 0 && comparable.compareTo(lessObject) < 0;
    }

    public static <T> boolean isBetweenMoreOrEqual(Comparable<T> comparable, T moreOrEqualObject, T lessObject) {
        return comparable.compareTo(moreOrEqualObject) >= 0 && comparable.compareTo(lessObject) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

