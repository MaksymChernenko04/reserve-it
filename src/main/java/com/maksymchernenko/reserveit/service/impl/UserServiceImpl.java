package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
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

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).get() ;
    }
}
