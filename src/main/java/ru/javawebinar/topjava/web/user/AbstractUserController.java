package ru.javawebinar.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    public List<User> getAll() {
        log.info("getAll");
        return this.userService.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return this.userService.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        return this.userService.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        this.userService.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        this.userService.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return this.userService.getByEmail(email);
    }
}