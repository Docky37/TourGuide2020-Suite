package com.tripmaster.gps;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripmaster.gps.controller.GpsController;

@SpringBootTest
class GpsToolsApplicationTests {

    @Autowired
    GpsController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
