package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.exceptions.WrongPasswordException;
import com.maksymchernenko.reserveit.model.User;

public interface UserService {

    User register(User user) throws UserAlreadyExistsException;
    User login(String email, String password) throws UserNotFoundException, WrongPasswordException;
}
