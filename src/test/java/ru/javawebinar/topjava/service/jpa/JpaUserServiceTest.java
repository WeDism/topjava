package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveProjectProfiles;
import ru.javawebinar.topjava.service.common.UserCommonServiceTest;

@ActiveProfiles(ActiveProjectProfiles.JPA)
public class JpaUserServiceTest extends UserCommonServiceTest {
}
