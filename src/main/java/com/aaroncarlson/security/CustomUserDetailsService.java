package com.aaroncarlson.security;

import com.aaroncarlson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CustomUserDetailsService loads a User's data given a username, email or id
 * NOTE: UserDetailService interface consists of a single method (loadUserByUsername) that loads a User based on
 * username/email (aka: credentials) . CustomUserDetailsService provides the implementation for the interface
 * method. The return from the interface (UserDetails object) is used by Spring Security for performing various
 * authentication and role based validations. UserDetails holds information about the User (such as username
 * and password).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String usernameOrEmail) throws UsernameNotFoundException {
        return UserPrincipal.create(userService.findUserByUsernameOrEmail(usernameOrEmail));
    }

    @Transactional
    public UserDetails loadUserById(final Long id) {
        return UserPrincipal.create(userService.findUserById(id));
    }

}
