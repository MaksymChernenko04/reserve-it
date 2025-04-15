package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.model.User;

public interface UserService {

    User register(User user) throws UserAlreadyExistsException;
    User getByEmail(String email);
}
