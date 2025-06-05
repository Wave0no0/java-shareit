package ru.practicum.shareit.user;

import ru.practicum.shareit.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(long id);

    List<User> findAll();

    void deleteById(long id);
}
