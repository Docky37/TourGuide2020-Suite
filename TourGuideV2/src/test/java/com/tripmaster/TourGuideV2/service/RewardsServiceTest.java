package com.tripmaster.TourGuideV2.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
import com.tripmaster.TourGuideV2.service.IRewardsService;
import com.tripmaster.TourGuideV2.service.RewardsService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(SpringExtension.class)
public class RewardsServiceTest {

    private static MockWebServer mockWebServer = new MockWebServer();

    IRewardsService rewardsService;

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

    }

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer.start();
        System.out.println("\n\n Port: " + mockWebServer.getPort() + "\n\n");
    }

    @BeforeEach
    void initialize() {
        rewardsService = new RewardsService("TEST", WebClient
                .create(mockWebServer
                        .url("http://localhost:" + mockWebServer.getPort())
                        .toString()));
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test // Here we test the calculateRewards and getRewardPoints methods
    @DisplayName("Given a nearby attraction when calculateRewards then 1 reward created")
    public void givenAVisitedLocationAndANearbyAttractions_whenCalculateRewards_thenUserRewardsListSizeIsEqualTo1()
            throws InterruptedException, ExecutionException {
        System.out.println(
                "\n\n *** Given a nearby attraction when calculateRewards"
                        + " then UserRewardsList size is equal to 1. ***");
        // GIVEN
        User user = new User(UUID.randomUUID(), "jon", "000",
                "jon@tourGuide.com");
        user.addToVisitedLocations(
                new VisitedLocation(user.getUserId(), new Location(
                        attractions.get(3).getLatitude(),
                        attractions.get(3).getLongitude()),
                        new Date()));

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE)
                        .setBody("77"));

        // WHEN
        rewardsService.calculateRewards(user, attractions);
        TimeUnit.SECONDS.sleep(1);
        List<UserReward> userRewards = user.getUserRewards();
        System.out.println(userRewards.size());
        // THEN
        assertThat(userRewards).size().isEqualTo(1);
    }

    @Test // Here we test isWithinAttractionProximity method
    @DisplayName("Given 1 Attraction, when IsWithinAttractionProximity of 2 Locations then first return true then false")
    public void givenAnAttraction_whenIsWithinAttractionProximityOf2Locations_thenFirstReturnTrueThenFalse() {
        System.out.println(
                "\n\n *** Given 1 Attraction, when IsWithinAttractionProximity of 2 Locations then first return true then false ***");
        /*
         * Here we test if the Eiffel tower isWithinAttractionProximity of the
         * Charles De Gaulle Terminal 2 -> Less than 16 miles, it's true!
         */
        assertThat(rewardsService.isWithinAttractionProximity(
                attractions.get(0), new Location(49.003199, 2.561925)))
                        .isTrue();

        /*
         * Here we test if the Eiffel tower isWithinAttractionProximity of
         * Saint-Jean-De-Luz -> About 420 miles, it's false!
         */
        assertThat(rewardsService.isWithinAttractionProximity(
                attractions.get(0), new Location(43.386897, -1.661847)))
                        .isFalse();
    }

    @Test // Here we test the getDistance method between to locations
    @DisplayName("Given 2 Locations, when GetDistance then return the rigth distance")
    public void given2Locations_whenGetDistance_thenReturnTheRigthDistance() {
        System.out.println(
                "\n\n *** Given 2 Locations, when GetDistance then return the rigth distance ***");
        /*
         * Here we calculate the distance between the Eiffel tower and the
         * Montparnasse Station. Google result is 1.67 miles
         */
        int distance = (int) Math.round(100 * rewardsService.getDistance(
                new Location(48.858482d, 2.294426d),
                new Location(48.841538, 2.320519)));
        assertThat(distance).isEqualTo(167);

        /*
         * Here we calculate the distance between the Eiffel tower and the
         * Saint-Jean-de-Luz. Google result is 422 miles
         */
        distance = (int) Math.round(
                rewardsService.getDistance(new Location(48.841538, 2.320519),
                        new Location(43.386897, -1.661847)));
        assertThat(distance).isEqualTo(422);
    }

    @Test // Here we test both calculate rewards and private nearAttraction
          // method
    @DisplayName("Given MaxValue for ProximityBuffer, whenGetAttractions then return all Attractions")
    public void givenAMaxIntProximityBuffer_whenCalculateRewards_thenReturnAllAttractions()
            throws InterruptedException, ExecutionException {
        System.out.println(
                "\n\n *** Given MaxValue for ProximityBuffer, whenGetAttractions then return all Attractions ***");
        // GIVEN
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);
        User user = new User(UUID.randomUUID(), "john", "000",
                "john@tourGuide.com");
        user.addToVisitedLocations(
                new VisitedLocation(user.getUserId(), new Location(
                        attractions.get(0).getLatitude(),
                        attractions.get(0).getLongitude()),
                        new Date()));
        attractions.forEach(a -> {
            mockWebServer.enqueue(
                    new MockResponse()
                            .setResponseCode(200)
                            .setHeader(HttpHeaders.CONTENT_TYPE,
                                    MediaType.APPLICATION_JSON_VALUE)
                            .setBody(String.valueOf(a.hashCode())));
        });

        // WHEN
        rewardsService.calculateRewards(user, attractions);
        List<UserReward> userRewards = user.getUserRewards();
        while(userRewards.size() < attractions.size()) {
            TimeUnit.MILLISECONDS.sleep(200);
            userRewards = user.getUserRewards();
        }
        
        // THEN
        System.out.println(userRewards.size());
        assertThat(userRewards.size()).isEqualTo(attractions.size());

    }
}
