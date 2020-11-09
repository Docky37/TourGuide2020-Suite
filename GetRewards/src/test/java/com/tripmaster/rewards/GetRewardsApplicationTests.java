package com.tripmaster.rewards;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripmaster.rewards.controller.RewardsController;

@SpringBootTest
class GetRewardsApplicationTests {

    @Autowired
    RewardsController controller;

    @Test
	void contextLoads() {
        assertThat(controller).isNotNull();
	}

}
