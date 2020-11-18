package com.tripmaster.tripdeals.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.List;
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

import com.tripmaster.tripdeals.dto.ProviderDTO;
import com.tripmaster.tripdeals.service.ITripDealService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TripDealsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITripDealService tripDealService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Given a userId when getTripDeals then call tripDealService")
    public void givenAUserId_getTripDeals_thenCallGetTripDealServiceMethod()
            throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        List<ProviderDTO> providers = new ArrayList<>();
        providers.add(new ProviderDTO(UUID.randomUUID(),
                "What's a wonderfull trip!", 777));
        providers.add(new ProviderDTO(UUID.randomUUID(),
                "Bad trip!", 1313));
        given(tripDealService.getTripDeals("test-server-api-key", userId, 2, 0,
                3, 5673)).willReturn(providers);
        // WHEN
        mvc.perform(
                MockMvcRequestBuilders.get("/getTripDeals?userId=" + userId
                        + "&tripPricerApiKey=test-server-api-key"
                        + "&numberOfAdult=2&numberOfChildren=0"
                        + "&tripDuration=3&cumulatativeRewardPoints=5673")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(tripDealService).getTripDeals(
                "test-server-api-key", userId, 2, 0, 3, 5673);
    }

    @Test
    @DisplayName("Given user without rewards when getTripDeals"
            + " then call tripDealService")
    public void givenAUserId2_getTripDeals_thenCallGetTripDealServiceMethod()
            throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        List<ProviderDTO> providers = new ArrayList<>();
        given(tripDealService.getTripDeals("test-server-api-key", userId, 2, 0,
                3, 0)).willReturn(providers);
        // WHEN
        mvc.perform(
                MockMvcRequestBuilders.get("/getTripDeals?userId=" + userId
                        + "&tripPricerApiKey=test-server-api-key"
                        + "&numberOfAdult=2&numberOfChildren=0"
                        + "&tripDuration=3&cumulatativeRewardPoints=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(tripDealService).getTripDeals(
                "test-server-api-key", userId, 2, 0, 3, 0);
    }

}
