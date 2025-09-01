package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.repository.UserRepository;
import com.maksymchernenko.reserveit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implements {@link UserService} interface.
 * <p>
 * Provides business logic methods to save and delete users, query users and roles.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Instantiates a new {@link UserService}.
     *
     * @param userRepository  the {@link UserRepository}
     * @param passwordEncoder the {@link PasswordEncoder}
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Getting all users");

        return userRepository.getAllUsers().orElseGet(ArrayList::new);
    }

    @Override
    public List<Role> getAllRoles() {
        logger.info("Getting all roles");

        return userRepository.getAllRoles().orElseGet(ArrayList::new);
    }

    @Override
    public User getByEmail(String email) throws UserNotFoundException {
        logger.info("Getting user with email = {}", email);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            logger.warn("User with email {} does not exist", email);

            throw new UserNotFoundException("User with given email does not exists");
        }


        return user.get();
    }

    @Transactional
    @Override
    public User register(User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("User with email {} already exists", user.getEmail());

            throw new UserAlreadyExistsException("User with given email already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.getRole() == null) {
                user.setRole(userRepository.findRoleByName("ROLE_CLIENT").orElseThrow());
            }

            logger.info("Creating user = {}", user);

            return userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void updateUser(User user) throws UserNotFoundException {
        this.getByEmail(user.getEmail());

        logger.info("Updating user = {}", user);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public boolean updatePassword(User user,
                                  String oldPassword,
                                  String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            logger.warn("Old password does not match with new password");

            return false;
        }
        else {
            logger.info("Updating password for user = {}", user);

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            return true;
        }
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        logger.info("Deleting user with id = {}", id);

        userRepository.delete(id);
    }
}
