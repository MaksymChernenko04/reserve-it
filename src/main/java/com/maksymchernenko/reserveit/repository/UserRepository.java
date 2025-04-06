package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);
    User save(User user);
}
