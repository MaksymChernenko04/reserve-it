package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    List<Role> getAllRoles();
    User register(User user) throws UserAlreadyExistsException;
    User getByEmail(String email) throws UserNotFoundException;
    void deleteUser(int id);
}
