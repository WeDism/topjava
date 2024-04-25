package ru.javawebinar.topjava.util;

import java.util.Arrays;
import java.util.List;

public final class UserSampleData {
    private UserSampleData() {
    }

    public static int FIRST_USER = 1;
    public static int SECOND_USER = 2;
    public static List<Integer> SAMPLE_USERS = Arrays.asList(FIRST_USER, SECOND_USER);
}
