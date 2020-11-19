package com.tripmaster.rewards.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tripmaster.rewards.service.IRewardsService;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RewardsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IRewardsService rewardsService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Given attractionId & userId when getRewardPoints then"
            + " call getRewardPoints service method ")
    public void givenAttractionIdAndUserId_whenGetRewardPoints_thenCallGetLocationServiceMethod()
            throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID attractionId = UUID.randomUUID();

        // WHEN
        mvc.perform(
                MockMvcRequestBuilders
                        .get("/getReward?attractionId=" + attractionId
                                + "&userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(rewardsService).getRewardPoints(attractionId, userId);
    }

}
