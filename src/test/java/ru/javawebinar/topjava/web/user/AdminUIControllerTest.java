package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.*;

class AdminUIControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Autowired
    private UserService userService;

    @Test
    void updateUserStatus() throws Exception {
        assumeDataJpa();
        perform(MockMvcRequestBuilders.put(AdminUIController.ADMIN_USERS + "/status/{id}", GUEST_ID)
                .param("isEnable", "false"))
                .andExpect(status().isNoContent())
                .andDo(print());
        guest.setEnabled(false);
        USER_MATCHER.assertMatch(userService.get(GUEST_ID), guest);
        perform(MockMvcRequestBuilders.put(AdminUIController.ADMIN_USERS + "/status/{id}", GUEST_ID)
                .param("isEnable", "true"))
                .andExpect(status().isNoContent())
                .andDo(print());
        guest.setEnabled(true);
        USER_MATCHER.assertMatch(userService.get(GUEST_ID), guest); // В базе лежат верные данные, но в базу данный сервис не ходит
    }
}