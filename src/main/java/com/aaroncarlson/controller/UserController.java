package com.aaroncarlson.controller;

import com.aaroncarlson.payload.UserIdentityAvailability;
import com.aaroncarlson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") final String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") final String email) {
        boolean isAvailable = userService.isEmailAvailable(email);
        return new UserIdentityAvailability(isAvailable);
    }

}
