package com.aaroncarlson.util;

import java.time.LocalDateTime;

public interface TestConstants {

    String AARON_CARLSON = "Aaron Carlson";
    String SYDNEY_MACBAIN = "Sydney MacBain";
    String DAISY_CARLSON = "Daisy Carlson";
    String ACARLSON = "acarlson";
    String SMACBAIN  = "smacbain";
    String DCARLSON = "dcarlson";
    String AARON_EMAIL = "acarlson@test.com";
    String SYDNEY_EMAIL = "smacbain@test.com";
    String DAISY_EMAIL = "dcarlson@test.com";
    String PASSWORD = "password";
    String SAN_FRANCISCO = "San Francisco";
    String BARCELONA = "Barcelona";
    String BERKELEY = "Berkeley";
    String WALNUT_CREEK = "Walnut Creek";
    String OAKLAND = "Oakland";
    String SAN_DIEGO = "San Diego";
    LocalDateTime NOW = LocalDateTime.now();
    LocalDateTime NOW_MINUS_ONE_MONTH = NOW.minusMonths(1);
    LocalDateTime NOW_PLUS_ONE_MONTH = NOW.plusMonths(1);
    LocalDateTime NOW_MINUS_TWO_MONTH = NOW.minusMonths(2);
    LocalDateTime NOW_PLUS_TWO_MONTH = NOW.plusMonths(2);
}
