package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.exceptions.UserNotFoundException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;

import java.util.List;

/**
 * Service interface for managing {@link User} and {@link Role} objects.
 * <p>
 * Defines business logic methods to save and delete users, query users and roles.
 */
public interface UserService {

    /**
     * Gets all users.
     *
     * @return the list of users
     */
    List<User> getAllUsers();

    /**
     * Gets all roles.
     *
     * @return the list of roles
     */
    List<Role> getAllRoles();

    /**
     * Registers a new user.
     *
     * @param user the new user
     * @return the saved user
     * @throws UserAlreadyExistsException if the user with a given email already exists
     */
    User register(User user) throws UserAlreadyExistsException;

    /**
     * Updates a user.
     *
     * @param user the updated user
     * @throws UserNotFoundException if the user with a given email is not found
     */
    void updateUser(User user) throws UserNotFoundException;

    /**
     * Gets a user by email.
     *
     * @param email the user email
     * @return the user
     * @throws UserNotFoundException if the user with a given email is not found
     */
    User getByEmail(String email) throws UserNotFoundException;

    /**
     * Updates a user password.
     *
     * @param user        the user to update
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    boolean updatePassword(User user,
                           String oldPassword,
                           String newPassword);

    /**
     * Deletes a user.
     *
     * @param id the user id
     */
    void deleteUser(int id);
}
