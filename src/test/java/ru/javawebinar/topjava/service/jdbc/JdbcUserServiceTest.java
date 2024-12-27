package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveProjectProfiles;
import ru.javawebinar.topjava.service.common.UserCommonServiceTest;

@ActiveProfiles(ActiveProjectProfiles.JDBC)
public class JdbcUserServiceTest extends UserCommonServiceTest {
}
