package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Optional<List<User>> getAllUsers();
    Optional<List<Role>> getAllRoles();
    Optional<User> findByEmail(String email);
    User saveUser(User user);
    Optional<User> deleteUser(int id);
}
