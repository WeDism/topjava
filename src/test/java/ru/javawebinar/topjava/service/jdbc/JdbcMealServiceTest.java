package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveProjectProfiles;
import ru.javawebinar.topjava.service.common.MealCommonServiceTest;

@ActiveProfiles(ActiveProjectProfiles.JDBC)
public class JdbcMealServiceTest extends MealCommonServiceTest {
}
