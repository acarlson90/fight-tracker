package com.aaroncarlson.service;

import com.aaroncarlson.model.Role;
import com.aaroncarlson.model.RoleName;
import com.aaroncarlson.model.User;
import com.aaroncarlson.repository.RoleRepository;
import com.aaroncarlson.util.TestConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    private User acarlson;
    private User smacbain;
    private User dcarlson;
    private RoleRepository roleRepository;

    @Before
    public void setup() throws Exception {
        acarlson = userService.createUser(TestConstants.AARON_CARLSON, TestConstants.ACARLSON, TestConstants.AARON_EMAIL, TestConstants.PASSWORD);
        smacbain = userService.createUser(TestConstants.SYDNEY_MACBAIN, TestConstants.SMACBAIN, TestConstants.SYDNEY_EMAIL, TestConstants.PASSWORD);
        dcarlson = userService.createUser(TestConstants.DAISY_CARLSON, TestConstants.DCARLSON, TestConstants.DAISY_EMAIL, TestConstants.PASSWORD);
    }

    @Test
    public void testAvailability() throws Exception {
        assertThat(userService.isUsernameAvailable(TestConstants.ACARLSON)).isFalse();
        assertThat(userService.isUsernameAvailable(TestConstants.ACARLSON.toUpperCase())).isFalse();
        assertThat(userService.isUsernameAvailable(TestConstants.ACARLSON + "12")).isTrue();
        assertThat(userService.isEmailAvailable(TestConstants.AARON_EMAIL)).isFalse();
        assertThat(userService.isEmailAvailable(TestConstants.AARON_EMAIL.toUpperCase())).isTrue();
    }

    @After
    public void cleanup() throws Exception {
        userService.deleteAllUsers();
    }

}
