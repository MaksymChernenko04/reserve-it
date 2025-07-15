package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.repository.AdminRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

    private final EntityManager entityManager;

    @Autowired
    public AdminRepositoryImpl(EntityManager entityManager) {
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
    public User saveUser(User user) {
        user.setPassword("{noop}" + user.getPassword());
        entityManager.persist(user);

        return entityManager.find(User.class, user.getId());
    }

    @Override
    public Optional<User> deleteUser(int id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);

        return user == null ? Optional.empty() : Optional.of(user);
    }
}
