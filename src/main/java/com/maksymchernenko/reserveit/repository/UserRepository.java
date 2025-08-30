package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link User} and {@link Role} entries in the database.
 * <p>
 * Provides methods to save and delete users, query users and roles.
 */
public interface UserRepository {

    /**
     * Gets all users.
     *
     * @return the optional list of users
     */
    Optional<List<User>> getAllUsers();

    /**
     * Gets all roles.
     *
     * @return the optional list of roles
     */
    Optional<List<Role>> getAllRoles();

    /**
     * Gets a user by email.
     *
     * @param email the user email
     * @return the optional of user
     */
    Optional<User> findByEmail(String email);

    /**
     * Gets a role by name.
     *
     * @param name the role name
     * @return the optional of a role
     */
    Optional<Role> findRoleByName(String name);

    /**
     * Saves a user.
     *
     * @param user the user
     * @return the saved user
     */
    User save(User user);

    /**
     * Deletes a user.
     *
     * @param id the user id
     */
    void delete(int id);
}
