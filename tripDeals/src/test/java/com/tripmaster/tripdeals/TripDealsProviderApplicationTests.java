package com.tripmaster.tripdeals;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripmaster.tripdeals.controller.TripDealsController;

@SpringBootTest
class TripDealsProviderApplicationTests {

    @Autowired
    TripDealsController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
