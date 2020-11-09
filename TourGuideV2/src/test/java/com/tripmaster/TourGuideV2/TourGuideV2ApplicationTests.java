package com.tripmaster.TourGuideV2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripmaster.TourGuideV2.controller.TourGuideController;

@SpringBootTest
class TourGuideV2ApplicationTests {

    @Autowired
    TourGuideController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
