package com.aaroncarlson.service;

import com.aaroncarlson.exception.AppException;
import com.aaroncarlson.model.Role;
import com.aaroncarlson.model.RoleName;
import com.aaroncarlson.model.User;
import com.aaroncarlson.repository.RoleRepository;
import com.aaroncarlson.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isUsernameAvailable(final String username) {
        // Username is case insensitive
        return !userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean isEmailAvailable(final String email) {
        return !userRepository.existsByEmail(email);
    }

    public User findUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    public User findUserByUsernameOrEmail(final String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

    public User createUser(final String name, final String username, final String email, final String password) {
        User user = new User(name, username, email, password);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set"));
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}
