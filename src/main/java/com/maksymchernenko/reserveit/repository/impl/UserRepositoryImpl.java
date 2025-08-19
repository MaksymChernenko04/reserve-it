package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<List<User>> getAllUsers() {
        List<User> users = entityManager
                .createQuery("FROM User", User.class)
                .getResultList();

        return users.isEmpty() ? Optional.empty() : Optional.of(users);
    }

    @Override
    public Optional<List<Role>> getAllRoles() {
        List<Role> roles = entityManager
                .createQuery("FROM Role", Role.class)
                .getResultList();

        return roles.isEmpty() ? Optional.empty() : Optional.of(roles);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> list = entityManager.createQuery("FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Role> findRoleByName(String name) {
        List<Role> list = entityManager.createQuery("FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) entityManager.persist(user);
        else entityManager.merge(user);

        return entityManager.find(User.class, user.getId());
    }

    @Override
    public void delete(int id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }
}
