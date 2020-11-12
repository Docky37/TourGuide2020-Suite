package com.tripmaster.TourGuideV2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.UserReward;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.NearbyAttractionDTO;
import com.tripmaster.TourGuideV2.dto.ProviderDTO;
import com.tripmaster.TourGuideV2.dto.UserRewardDTO;
import com.tripmaster.TourGuideV2.dto.UserRewardsDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;
import com.tripmaster.TourGuideV2.service.IRewardsService;
import com.tripmaster.TourGuideV2.service.ITourGuideService;
import com.tripmaster.TourGuideV2.service.TourGuideService;
import com.tripmaster.TourGuideV2.tracker.Tracker;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TourGuideControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITourGuideService tourGuideService;

    @MockBean
    private IRewardsService rewardsService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Given a user, when GetLocation then calls service getUserLocation method")
    public void givenAUser_whenGetLocation_thenCallsServiceGetUserLocationMethod()
            throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        String userName = "John DOE";
        User user = new User(userId, userName, "1234567890",
                "John.Doe@mail.com");
        given(tourGuideService.getUser(userName)).willReturn(user);
        given(tourGuideService.getUserLocation(user))
                .willReturn(new VisitedLocationDTO(new LocationDTO(45.0, 1.0),
                        new Date(), userId));
        // WHEN
        mvc.perform(
                MockMvcRequestBuilders.get("/getLocation?userName=" + userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(tourGuideService).getUserLocation(user);
    }

    @Test
    @DisplayName("When request getNearbyAttractions then calls Service getAttractionsSuggestion")
    public void givenAUser_whenGetNearbyAttractions_thenCallsServiceGetAttractionsSuggestion()
            throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        String userName = "John DOE";
        User user = new User(userId, userName, "1234567890",
                "John.Doe@mail.com");
        AttractionsSuggestionDTO suggestion = new AttractionsSuggestionDTO();
        suggestion.setUserLocation(new LocationDTO(45.0, 1.0));
        suggestion.setSuggestedAttractions(
                new HashMap<String, NearbyAttractionDTO>());
        given(tourGuideService.getUser(userName)).willReturn(user);
        given(tourGuideService.getAttractionsSuggestion(user))
                .willReturn(suggestion);
        // WHEN
        mvc.perform(MockMvcRequestBuilders
                .get("/getNearbyAttractions?userName=" + userName)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(tourGuideService).getAttractionsSuggestion(user);
    }

    @Test
    @DisplayName("Given a user, when GetRewards then calls service GetUserRewards method ")
    public void givenAUser_whenGetRewards_thenCallsServiceGetRewardsMethod()
            throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        String userName = "John DOE";
        User user = new User(userId, userName, "1234567890",
                "John.Doe@mail.com");
        tourGuideService.addUser(user);
        UserRewardsDTO userRewardsDTO = new UserRewardsDTO();
        userRewardsDTO.setUserName(userName);
        userRewardsDTO.addUserRewardDTO(new UserRewardDTO(
                new VisitedLocation(new Location(45.0, 1.0), new Date()),
                new Attraction(UUID.randomUUID(), "Tour Eiffel",
                        "Paris", "France", 48.858482d, 2.294426d),
                77));
        ;
        given(tourGuideService.getUser(userName)).willReturn(user);
        given(tourGuideService.getUserRewards(user)).willReturn(userRewardsDTO);

        // WHEN
        mvc.perform(MockMvcRequestBuilders
                .get("/getRewards?userName=" + userName)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(tourGuideService).getUserRewards(user);
    }

    @Test
    public void whenRequestAllCurrentLocations_thenCallsServiceGetAllUsersLocations() throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        String userName = "John DOE";
        User user = new User(userId, userName, "1234567890",
                "John.Doe@mail.com");
        tourGuideService.addUser(user);
        // WHEN
        mvc.perform(MockMvcRequestBuilders
                .get("/getAllCurrentLocations")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(tourGuideService).getAllUsersLocation();

    }

    @Test
    public void givenAUser_whenRequestGetTripDeals_thenCallsServiceGetTripDeals() throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        String userName = "John DOE";
        User user = new User(userId, userName, "1234567890",
                "John.Doe@mail.com");
        UserReward reward = new UserReward(null, null, 77);
        user.addUserReward(reward);
        List<ProviderDTO> providers = new ArrayList<>();
        given(tourGuideService.getUser(userName)).willReturn(user);
        given(tourGuideService.getTripDeals(user)).willReturn(providers);
        // WHEN
        mvc.perform(MockMvcRequestBuilders
                .get("/getTripDeals?userName=" + userName)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(tourGuideService).getTripDeals(user);

    }

}
