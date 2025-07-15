package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.exceptions.UserAlreadyExistsException;
import com.maksymchernenko.reserveit.model.Role;
import com.maksymchernenko.reserveit.model.User;
import com.maksymchernenko.reserveit.repository.AdminRepository;
import com.maksymchernenko.reserveit.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return adminRepository.getAllUsers().orElseGet(ArrayList::new);
    }

    @Override
    public List<Role> getAllRoles() {
        return adminRepository.getAllRoles().orElseGet(ArrayList::new);
    }

    @Transactional
    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        if (adminRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with given email already exists");
        } else {
            return adminRepository.saveUser(user);
        }
    }

    @Transactional
    @Override
    public Optional<User> deleteUser(int id) {
        return adminRepository.deleteUser(id);
    }
}
