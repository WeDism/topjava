package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return this.userRepository.save(user);
    }

    public void delete(int id) {
        ValidationUtil.checkNotFoundWithId(this.userRepository.delete(id), id);
    }

    public User get(int id) {
        return ValidationUtil.checkNotFoundWithId(this.userRepository.get(id), id);
    }

    public User getByEmail(String email) {
        return ValidationUtil.checkNotFound(this.userRepository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return this.userRepository.getAll();
    }

    public void update(User user) {
        ValidationUtil.checkNotFoundWithId(this.userRepository.save(user), user.getId());
    }
}