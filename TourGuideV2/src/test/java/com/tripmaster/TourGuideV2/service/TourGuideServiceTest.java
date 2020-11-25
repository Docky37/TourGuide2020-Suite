package com.tripmaster.TourGuideV2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import com.tripmaster.TourGuideV2.dto.UserPreferencesDTO;
import com.tripmaster.TourGuideV2.dto.UserRewardsDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;
import com.tripmaster.TourGuideV2.helper.InternalTestHelper;
import com.tripmaster.TourGuideV2.service.IRewardsService;
import com.tripmaster.TourGuideV2.service.ITourGuideService;
import com.tripmaster.TourGuideV2.service.TourGuideService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.springframework.boot.test.mock.mockito.MockBean;

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
        mockWebServerGps.start();
        mockWebServerTripDeals.start();
        System.out.println("\n\n -> Port for Gps: " + mockWebServerGps.getPort()
                + "\n -> Port for TripDeals: "
                + mockWebServerTripDeals.getPort() + "\n");
    }

    @BeforeEach
    void initialize() {
        InternalTestHelper.setInternalUserNumber(0);
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
    @DisplayName("Given a registred userName, when addUser with same username then User is not registred")
    void givenAUser_whenAddUser_thenUserIsNotSaved() {
        System.out.println(
                "\n *** Given a registred userName, when addUser with same username then User is not saved ***");
        // GIVEN
        User user = new User(UUID.randomUUID(), "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "John DOE",
                "01.02.03.06.07",
                "jojo@javavousledire.com");
        tourGuideService.addUser(user);
        User retrivedUser = tourGuideService.getUser(user.getUserName());
        assertThat(retrivedUser).isEqualTo(user);
        // WHEN
        tourGuideService.addUser(user2);
        List<User> allUsers = tourGuideService.getAllUsers();
        // THEN
        assertThat(allUsers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Given few users, when call getAllUsers method then return User List")
    void givenFewUsers_whenGetAllUsers_thenReturnsAllUsersList() {
        System.out.println(
                "\n *** Given few users, when call getAllUsers method then return User List ***");
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
    @DisplayName("Given a user, when call updateUserPreferences method"
            + " then preferences are updated")
    public void givenAUser_whenUpdateUserPreferences_thenUpdatePreferences() {
        System.out.println(
                "\n*** Given a user, when call updateUserPreferences method "
                        + "then preferences are updated ***");
        // GIVEN
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");
        assertThat(user.getUserPreferences().getAttractionProximity())
                .isEqualTo(Integer.MAX_VALUE);
        assertThat(user.getUserPreferences().getCurrency())
                .isEqualTo(Monetary.getCurrency("USD"));
        assertThat(user.getUserPreferences().getNumberOfAdults())
                .isEqualTo(1);
        assertThat(user.getUserPreferences().getNumberOfChildren())
                .isEqualTo(0);
        assertThat(user.getUserPreferences().getHighPricePoint())
                .isEqualTo(Money.of(Integer.MAX_VALUE,
                        Monetary.getCurrency("USD")));
        assertThat(user.getUserPreferences().getLowerPricePoint())
                .isEqualTo(Money.of(0, Monetary.getCurrency("USD")));
        assertThat(user.getUserPreferences().getTicketQuantity())
                .isEqualTo(1);
        assertThat(user.getUserPreferences().getTripDuration())
                .isEqualTo(1);

        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        userPreferencesDTO.setAttractionProximity(150);
        userPreferencesDTO.setNumberOfAdults(2);
        userPreferencesDTO.setHighPricePoint(650);
        // WHEN
        tourGuideService.updateUserPreferences(user, userPreferencesDTO);
        // THEN
        assertThat(user.getUserPreferences().getAttractionProximity())
                .isEqualTo(userPreferencesDTO.getAttractionProximity());
        assertThat(user.getUserPreferences().getNumberOfAdults())
                .isEqualTo(userPreferencesDTO.getNumberOfAdults());
        assertThat(user.getUserPreferences().getNumberOfChildren())
                .isEqualTo(userPreferencesDTO.getNumberOfChildren());
        assertThat(user.getUserPreferences().getHighPricePoint().getNumber()
                .intValue())
                        .isEqualTo(userPreferencesDTO.getHighPricePoint());
        assertThat(user.getUserPreferences().getLowerPricePoint().getNumber()
                .intValue())
                        .isEqualTo(userPreferencesDTO.getLowerPricePoint());
        assertThat(user.getUserPreferences().getTicketQuantity())
                .isEqualTo(userPreferencesDTO.getTicketQuantity());
        assertThat(user.getUserPreferences().getTripDuration())
                .isEqualTo(userPreferencesDTO.getTripDuration());
    }

    @Test
    @DisplayName("Given a user, when call trackUser method then return his visitedLocation")
    public void givenAUser_whenTrackUser_thenReturnsHisVisitedLocation() {
        System.out.println(
                "\n*** Given a user, when call trackUser method then return his visitedLocation ***");
        // GIVEN
        UUID userId = UUID.randomUUID();
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
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
                new Location(45d, 1d), new Date()));

        // WHEN
        AttractionsSuggestionDTO suggestion = tourGuideService
                .getAttractionsSuggestion(user);

        // THEN
        assertThat(suggestion.getUserLocation().getLatitude())
                .isEqualTo(user.getLastVisitedLocation().getLocation()
                        .getLatitude());
        assertThat(suggestion.getUserLocation().getLongitude())
                .isEqualTo(user.getLastVisitedLocation().getLocation()
                        .getLongitude());
        assertThat(suggestion.getSuggestedAttraction().size())
                .isEqualTo(ITourGuideService.SIZE_OF_NEARBY_ATTRACTIONS_LIST);
    }

    @Test
    @DisplayName("Given a user, when getTripDeals then returns a list of five providers")
    public void givenAUser_whenGetTripDeals_thenReturnsAFiveProviderList() {
        System.out.println(
                "\n *** Given a user, when getTripDeals then returns a list of five providers ***");
        // GIVEN
        jsonResult = "["
                + "{\"tripId\":\"f815142d-5f6d-4568-8ff9-d573bfe5532d\","
                + "\"name\":\"Enterprize Ventures Limited\",\"price\":0.9899999999999949},"
                + "{\"tripId\":\"f815142d-5f6d-4568-8ff9-d573bfe5532d\",\"name\":\"Live Free\","
                + "\"price\":445.99},{\"tripId\":\"f815142d-5f6d-4568-8ff9-d573bfe5532d\","
                + "\"name\":\"United Partners Vacations\",\"price\":266.99},"
                + "{\"tripId\":\"f815142d-5f6d-4568-8ff9-d573bfe5532d\","
                + "\"name\":\"AdventureCo\",\"price\":258.99},"
                + "{\"tripId\":\"f815142d-5f6d-4568-8ff9-d573bfe5532d\","
                + "\"name\":\"Dream Trips\",\"price\":430.99}]";
        System.out.println(jsonResult);

        mockWebServerTripDeals.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult));

        User user = new User(UUID.randomUUID(), "jon", "000",
                "jon@tourGuide.com");
        // WHEN
        List<ProviderDTO> providers = tourGuideService.getTripDeals(user);
        // tourGuideService.tracker.stopTracking();
        // THEN
        assertThat(providers.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Given a user without rewards, when getTripDeals then returns an empty list")
    public void givenAUserWithoutRewards_whenGetTripDeals_thenReturnsEmptyList() {
        System.out.println(
                "\n *** Given a user without rewards, when getTripDeals then returns an empty list ***");
        // GIVEN

        mockWebServerTripDeals.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setHeader(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE)
                        .setBody(""));

        User user = new User(UUID.randomUUID(), "jon", "000",
                "jon@tourGuide.com");
        // WHEN
        List<ProviderDTO> providers = tourGuideService.getTripDeals(user);
        // tourGuideService.tracker.stopTracking();
        // THEN
        assertThat(providers.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Given users, when call getAllUsersLocation then returns users location list")
    public void givenUsers_whenGetAllUsersLocation_thenReturnsUsersLocationList() {
        System.out.println(
                "\n*** Given users, when call getAllUsersLocation then returns users location list ***");
        // GIVEN

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

    @Test
    @DisplayName("Given a new user, when call getUserLocation method then a new visitedLocation is created")
    void givenNewUser_whenGetUserLocation_thenANewVisitedLocationIsCreated() {
        System.out.println(
                "\n *** Given a new user, when call getUserLocation method then a new visitedLocation is created ***");
        // GIVEN
        User user = new User(UUID.randomUUID(), "John DOE", "01.02.03.04.05",
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

        // WHEN
        VisitedLocationDTO visitedLocationDTO = tourGuideService
                .getUserLocation(user);
        // THEN
        assertThat(visitedLocationDTO.getLocation().getLatitude()).isEqualTo(
                user.getLastVisitedLocation().getLocation().getLatitude());
        assertThat(visitedLocationDTO.getLocation().getLongitude()).isEqualTo(
                user.getLastVisitedLocation().getLocation().getLongitude());
        assertThat(visitedLocationDTO.getTimeVisited()).isEqualTo(
                user.getLastVisitedLocation().getTimeVisited());
        verify(rewardsService).calculateRewards(any(User.class),
                Mockito.<Attraction>anyList());
    }

    @Test
    @DisplayName("Given a user, when call getUserRewards method then returns user rewards")
    void givenAUser_whenGetUserRewards_thenReturnsUserRewards() {
        System.out.println(
                "\n *** Given a user, when call getUserRewards method then returns user rewards ***");
        // GIVEN
        User user = new User(UUID.randomUUID(), "John DOE", "01.02.03.04.05",
                "john.doe@tourGuide.com");

        given(rewardsService.getRewardPoints(any(Attraction.class),
                any(User.class))).willReturn(77);

        user.addUserReward(new UserReward(new VisitedLocation(user.getUserId(),
                new Location(attractions.get(0).getLatitude(),
                        attractions.get(0).getLongitude()),
                new Date()), attractions.get(0)));
        user.addUserReward(new UserReward(new VisitedLocation(user.getUserId(),
                new Location(attractions.get(1).getLatitude(),
                        attractions.get(1).getLongitude()),
                new Date()), attractions.get(1)));

        // WHEN
        UserRewardsDTO userRewardsDTO = tourGuideService.getUserRewards(user);
        // THEN
        assertThat(userRewardsDTO.getUserName()).isEqualTo("John DOE");
        assertThat(userRewardsDTO.getUserRewardsDTO().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Given n users to create, when call classInitialization then n users are created")
    void givenXUsersToCreate_whenCallClassInitialization_thenXUsersHaveBeenCreated() {
        System.out.println(
                "\n *** Given n users to create, when call classInitialization"
                        + " then n users with 3 visitedLocations are created ***");
        // GIVEN
        TourGuideService tourGuideService2 = (TourGuideService) tourGuideService;
        InternalTestHelper.setInternalUserNumber(7);

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

        // WHEN
        tourGuideService2.classInitialization();
        // THEN
        List<User> allUsers = tourGuideService2.getAllUsers();
        assertThat(allUsers.size()).isEqualTo(7);
        allUsers.forEach(
                u -> assertThat(u.getVisitedLocations().size()).isEqualTo(3));
    }

}
