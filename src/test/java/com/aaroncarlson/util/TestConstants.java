package com.aaroncarlson.util;

import java.time.LocalDateTime;

public interface TestConstants {

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
