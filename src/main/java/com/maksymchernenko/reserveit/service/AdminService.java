package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<User> getAllUsers();
    List<Role> getAllRoles();
    User createUser(User user) throws UserAlreadyExistsException;
    Optional<User> deleteUser(int id);
}
