package com.tripmaster.TourGuideV2;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.UserReward;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.ProviderDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;
import com.tripmaster.TourGuideV2.helper.InternalTestHelper;
import com.tripmaster.TourGuideV2.service.IRewardsService;
import com.tripmaster.TourGuideV2.service.ITourGuideService;
import com.tripmaster.TourGuideV2.service.RewardsService;
import com.tripmaster.TourGuideV2.service.TourGuideService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.io.Buffer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.service.IRewardsService;

@ExtendWith(SpringExtension.class)
public class TourGuideServiceTest {

    private static MockWebServer mockWebServerGps = new MockWebServer();
    private static MockWebServer mockWebServerTripDeals = new MockWebServer();

    private String jsonResult;
    private String visitedLocationResult;

    @MockBean
    IRewardsService rewardsService;

    ITourGuideService tourGuideService;

    static List<Attraction> attractions = new ArrayList<>();
    static {
        Locale.setDefault(Locale.US);
        attractions.add(new Attraction(UUID.randomUUID(), "Tour Eiffel",
                "Paris", "France", 48.858482d, 2.294426d));
        attractions.add(new Attraction(UUID.randomUUID(), "Futuroscope",
                "Chasseneuil-du-Poitou", "France", 46.669752d, 0.368955d));
        attractions.add(new Attraction(UUID.randomUUID(), "Notre Dame",
                "Paris", "France", 48.853208d, 2.348640d));
        attractions.add(new Attraction(UUID.randomUUID(), "Musée Automobile",
                "Vernon", "France", 46.441387, 0.475771));
    }

    @BeforeAll
    static void setUp() throws IOException {
        InternalTestHelper.setInternalUserNumber(0);
        mockWebServerGps.start();
        mockWebServerTripDeals.start();
        System.out.println("\n\n -> Port for Gps: " + mockWebServerGps.getPort()
                + "\n -> Port for TripDeals: "
                + mockWebServerTripDeals.getPort() + "\n");
    }

    @BeforeEach
    void initialize() {
        jsonResult = "[";
        attractions.forEach(
                a -> jsonResult = jsonResult.concat(a.toString() + ","));
        jsonResult = jsonResult.substring(0,
                jsonResult.length() - 1);
        jsonResult = jsonResult.concat("]");
        System.out.println(jsonResult);

        mockWebServerGps.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult));

        tourGuideService = new TourGuideService("  *** TEST ***",
                rewardsService,
                WebClient.create(mockWebServerTripDeals
                        .url("http://localhost:"
                                + mockWebServerTripDeals.getPort())
                        .toString()),
                WebClient.create(mockWebServerGps
                        .url("http://localhost:" + mockWebServerGps.getPort())
                        .toString()));
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServerTripDeals.shutdown();
        mockWebServerGps.shutdown();
    }

    @Test
    @DisplayName("Given a user with recorded visitedLocation, when getUserLocation"
            + " then returns his last visited location")
    void givenAUserWithVisitedLoc_whenGetUserLocation_thenReturnsHisLastVisitedLoc() {
        System.out.println(
                "\n *** Given a user with recorded visitedLocation, when getUserLocation,"
                        + " then returns his last visited location ***");
        // GIVEN

        User user = new User(UUID.randomUUID(), "jon", "000",
                "jon@tourGuide.com");
        // WHEN
        user.addToVisitedLocations(
                new VisitedLocation(user.getUserId(), new Location(
                        attractions.get(0).getLatitude(),
                        attractions.get(0).getLongitude()),
                        new Date()));
        user.addToVisitedLocations(
                new VisitedLocation(user.getUserId(), new Location(
                        attractions.get(1).getLatitude(),
                        attractions.get(1).getLongitude()),
                        new Date()));

        VisitedLocationDTO visitedLocation = tourGuideService
                .getUserLocation(user);
        // THEN
        assertThat(visitedLocation.getLocation().getLatitude())
                .isEqualTo(user.getVisitedLocations().get(1).getLocation()
                        .getLatitude());
        assertThat(visitedLocation.getLocation().getLongitude())
                .isEqualTo(user.getVisitedLocations().get(1).getLocation()
                        .getLongitude());
    }

    @Test
    @DisplayName("Given a user, when call addUser method then User is registred")
    void givenAUser_whenAddUser_thenUserIsRegistred() {
        System.out.println(
                "\n *** Given a user, when call addUser method then User is registred ***");
        // GIVEN
        User user = new User(UUID.randomUUID(), "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "Elvis PRESLEY",
                "01.02.03.04.05",
                "elvis@music.com");
        // WHEN
        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);
        // THEN
        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());
        assertThat(retrivedUser).isEqualTo(user);
        assertThat(retrivedUser2).isEqualTo(user2);
    }

    @Test
    @DisplayName("Given few users, when call addUser method then return User List")
    void givenFewUsers_whenGetAllUsers_thenReturnsAllUsersList() {
        System.out.println(
                "\n *** givenFewUsers_whenGetAllUsers_thenReturnsAllUsersList ***");
        // GIVEN
        User user = new User(UUID.randomUUID(), "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "Elvis PRESLEY",
                "01.02.03.04.05",
                "elvis@music.com");
        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);
        // WHEN
        List<User> allUsers = tourGuideService.getAllUsers();
        // THEN
        assertThat(allUsers).containsExactly(user, user2);
    }

    @Test
    @DisplayName("Given a user, when call trackUser method then return his visitedLocation")
    public void givenAUser_whenTrackUser_thenReturnsHisVisitedLocation() {
        System.out.println(
                "\n*** Given a user, when call trackUser method then return his visitedLocation ***");
        UUID userId = UUID.randomUUID();
        // GIVEN
        User user = new User(userId, "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");
        visitedLocationResult = "{\"timeVisited\":\"2020-11-02T21:53:25.603+00:00\","
                + "\"location\":{\"latitude\":-42.182901,\"longitude\":39.996201},"
                + "\"userId\":\"" + user.getUserId() + "\"}";

        mockWebServerGps.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE)
                        .setBody(visitedLocationResult));

        tourGuideService.addUser(user);
        // WHEN
        VisitedLocationDTO visitedLocationDTO = tourGuideService
                .trackUserLocation(user);
        // THEN
        assertThat(visitedLocationDTO.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    @DisplayName("Given a user, when call GetNearbyAttractions then returns a list of n attractions")
    public void givenAUser_whenGetNearbyAttractions_thenReturnAListOfNAttractions() {
        System.out.println(
                "\n***  ***");
        // GIVEN
        attractions.add(new Attraction(UUID.randomUUID(), "Clos Lucé",
                "Amboise", "France", 47.410445, 0.991830));
        attractions.add(
                new Attraction(UUID.randomUUID(), "Eglise Saint-Jean-Baptiste",
                        "Saint-Jean-de-Luz", "France", 43.386897, -1.661847));
        attractions.add(new Attraction(UUID.randomUUID(), "La Rhune",
                "Ascain", "France", 43.309685, -1.635410));
        attractions.add(new Attraction(UUID.randomUUID(), "Grand place",
                "Arras", "France",
                50.292564, 2.781040));

        jsonResult = "[";
        attractions.forEach(
                a -> jsonResult = jsonResult.concat(a.toString() + ","));
        jsonResult = jsonResult.substring(0,
                jsonResult.length() - 1);
        jsonResult = jsonResult.concat("]");
        System.out.println(jsonResult);

        mockWebServerGps.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult));

        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),
                new Location(45d, 1d), new Date());

        // WHEN
        List<Attraction> attractions = tourGuideService
                .getNearByAttractions(visitedLocation);

        // THEN
        assertThat(attractions.size())
                .isEqualTo(ITourGuideService.SIZE_OF_NEARBY_ATTRACTIONS_LIST);

        do {
            attractions.remove(attractions.get(attractions.size() - 1));
        } while (attractions.size() > 4);
             
   System.out.println(attractions.toString());
    }

    @Test
    @DisplayName("Given a user when Get Suggestions thenReturnSuggestions")
    public void givenAUser_whenGetSuggestions_thenReturnSuggestions() {
        System.out.println(
                "\n*** Given a user when Get Suggestions thenReturnSuggestions ***");

        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");
        tourGuideService.trackUserLocation(user);
        // WHEN
        AttractionsSuggestionDTO suggestion = tourGuideService
                .getAttractionsSuggestion(user);

        // tourGuideService.tracker.stopTracking();
        // THEN
        assertThat(suggestion.getUserLocation())
                .isEqualTo(user.getLastVisitedLocation().getLocation());
        assertThat(suggestion.getSuggestedAttraction().size())
                .isEqualTo(ITourGuideService.SIZE_OF_NEARBY_ATTRACTIONS_LIST);
    }

    @Ignore
    @Test
    public void givenAUser_whenGetTripDeals_thenReturnsAFiveProviderList() {
        System.out.println(
                "\n *** givenAUser_whenGetTripDeals_thenReturnsAFiveProviderList ***");
        // GIVEN

        User user = new User(UUID.randomUUID(), "jon", "000",
                "jon@tourGuide.com");
        // WHEN
        List<ProviderDTO> providers = tourGuideService.getTripDeals(user);
        // tourGuideService.tracker.stopTracking();
        // THEN
        assertThat(providers.size()).isEqualTo(5);
    }

    @Ignore
    @Test
    public void givenUsers_whenGetAllUsersLocation_thenReturnsUsersLocationList() {
        System.out.println(
                "\n*** givenUsers_whenGetAllUsersLocation_thenReturnsUsersLocationList ***");
        // GIVEN
        // tourGuideService.tracker.stopTracking();

        // WHEN
        Map<String, LocationDTO> locations = tourGuideService
                .getAllUsersLocation();
        // THEN
        locations.forEach((k, v) -> {
            System.out.println("user = " + k + ": latitude = " + v.getLatitude()
                    + " & longitude = " + v.getLongitude());
        });
        assertThat(locations.size())
                .isEqualTo(InternalTestHelper.getInternalUserNumber());
    }

}
