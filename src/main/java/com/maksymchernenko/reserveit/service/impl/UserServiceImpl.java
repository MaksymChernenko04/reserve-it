package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.exceptions.WrongPasswordException;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.repository.UserRepository;
import com.maksymchernenko.reserveit.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User register(User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with given email already exists");
        } else {
            return userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public User login(String email, String password) throws UserNotFoundException, WrongPasswordException {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new UserNotFoundException("User with given email doesn't exist");
        }

        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }
}
