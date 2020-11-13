package com.tripmaster.gps;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripmaster.gps.controller.GpsController;

@SpringBootTest
class GpsToolsApplicationTests {

    /**
     * GpsController instance declaration, that will be initialized by Spring
     * injection.
     */
    @Autowired
    private GpsController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
    
    @Test
    void mainMethod() {
        GpsToolsApplication.main(new String[] {"arg1", "arg2", "arg3"});
    }
}
