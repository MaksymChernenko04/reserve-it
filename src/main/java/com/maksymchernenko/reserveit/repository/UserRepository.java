package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<List<User>> getAllUsers();
    Optional<List<Role>> getAllRoles();
    Optional<User> findByEmail(String email);
    Optional<Role> findRoleByName(String name);
    User save(User user);
    void delete(int id);
}
