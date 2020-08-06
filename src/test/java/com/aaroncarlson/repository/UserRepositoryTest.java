package com.aaroncarlson.repository;

import com.aaroncarlson.model.User;
import com.aaroncarlson.util.TestConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User acarlson;
    private User smacbain;
    private User dcarlson;

    @Before
    public void setup() {
        acarlson = userRepository.save(new User(TestConstants.AARON_CARLSON, TestConstants.ACARLSON, TestConstants.AARON_EMAIL, TestConstants.PASSWORD));
        smacbain = userRepository.save(new User(TestConstants.SYDNEY_MACBAIN, TestConstants.SMACBAIN, TestConstants.SYDNEY_EMAIL, TestConstants.PASSWORD));
        dcarlson = userRepository.save(new User(TestConstants.DAISY_CARLSON, TestConstants.DCARLSON, TestConstants.DAISY_EMAIL, TestConstants.PASSWORD));
    }

    @Test
    public void testAvailability() throws Exception {
        assertThat(userRepository.existsByEmail(TestConstants.AARON_EMAIL.toUpperCase())).isFalse();
        assertThat(userRepository.existsByEmail(TestConstants.AARON_EMAIL)).isTrue();

        assertThat(userRepository.existsByUsernameIgnoreCase(TestConstants.ACARLSON.toUpperCase())).isTrue();
        assertThat(userRepository.existsByUsernameIgnoreCase(TestConstants.ACARLSON)).isTrue();
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

}
