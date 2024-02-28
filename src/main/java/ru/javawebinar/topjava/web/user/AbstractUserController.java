package ru.javawebinar.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return this.service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return this.service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return this.service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        this.service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        this.service.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return this.service.getByEmail(email);
    }
}