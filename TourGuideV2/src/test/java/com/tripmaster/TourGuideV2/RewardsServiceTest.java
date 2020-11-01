package com.tripmaster.TourGuideV2;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.extension.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
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
//@ExtendWith(MockitoExtension.class)
public class RewardsServiceTest {

    private static MockWebServer mockWebServer = new MockWebServer();

    IRewardsService rewardsService = new RewardsService(WebClient
            .create(mockWebServer.url("http://localhost:8787").toString()));

    static List<Attraction> attractions = new ArrayList<>();
    static {
        Locale.setDefault(Locale.US);
        attractions.add(new Attraction(UUID.randomUUID(), "Tour Eiffel",
                "Paris", "France",
                48.858482d, 2.294426d));
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer.start();
    }
    
    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    
    @Test
    public void givenAUser_whenUserGetRewards_thenUserRewardsListIsNotEmpty() {
        System.out.println(
                "\n\n *** givenAUser_whenUserGetRewards_thenUserRewardsListIsNotEmpty ***");
        // GIVEN

        User user = new User(UUID.randomUUID(), "jon", "000",
                "jon@tourGuide.com");

        user.addToVisitedLocations(
                new VisitedLocation(user.getUserId(),
                        new Location(attractions.get(0).getLatitude(),
                                attractions.get(0).getLongitude()),
                        new Date()));

        // WHEN
        rewardsService.calculateRewards(user, attractions);
        // THEN
        List<UserReward> userRewards = user.getUserRewards();
        assertThat(userRewards).size().isEqualTo(1);
    }

    @Test
    public void givenAnAttraction_whenIsWithinAttractionProximityOfItSelf_thenReturnTrue() {
        System.out.println(
                "\n\n *** givenAnAttraction_whenIsWithinAttractionProximityOfItSelf_thenReturnTrue ***");
        // GIVEN
        IRewardsService rewardsService = new RewardsService();
        // WHEN - THEN
        assertThat(rewardsService.isWithinAttractionProximity(
                attractions.get(0), new Location(
                        attractions.get(0).getLatitude(),
                        attractions.get(0).getLongitude()))).isTrue();
    }

    @Test
    public void givenAMaxIntProximityBuffer_whenCalculateRewards_thenReturnAllAttractions()
            throws InterruptedException, ExecutionException {
        System.out.println(
                "\n\n *** givenAMaxIntProximityBuffer_whenGetAttractions_thenReturnAllAttractions ***");
        // GIVEN
        User user = new User(UUID.randomUUID(), "jon", "000",
                "jon@tourGuide.com");
        user.addToVisitedLocations(
                new VisitedLocation(user.getUserId(), new Location(
                        attractions.get(0).getLatitude(),
                        attractions.get(0).getLongitude()),
                        new Date()));

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"reward\": 177}")
        );

        
        // WHEN
        CompletableFuture<?> result = rewardsService.calculateRewards(user,
                attractions);
        assertThat(result).isNotNull().isInstanceOf(CompletableFuture.class);
        assertThat(result.get()).isInstanceOf(User.class);
        List<UserReward> userRewards = user.getUserRewards();
        System.out.println(userRewards.size());
        // THEN
        // assertThat(userRewards.size()).isEqualTo(attractions.size());

    }

    /*
     * @Test public void
     * givenAMaxIntProximityBuffer_whenGetAttractions_thenReturnAllAttractions()
     * throws InterruptedException { System.out.println(
     * "\n\n *** givenAMaxIntProximityBuffer_whenGetAttractions_thenReturnAllAttractions ***"
     * ); IGpsService gpsService = new GpsService(); IRewardsService
     * rewardsService = new RewardsService(gpsService);
     * rewardsService.setProximityBuffer(Integer.MAX_VALUE);
     * 
     * User user = new User(UUID.randomUUID(), "jon", "000",
     * "jon@tourGuide.com"); user.addToVisitedLocations( new
     * VisitedLocation(user.getUserId(), attractions.get(0), new Date()));
     * 
     * List<CompletableFuture<Void>> futures = new ArrayList<>();
     * ExecutorService executorService = Executors.newFixedThreadPool(10);
     * futures.add(CompletableFuture.runAsync(() -> {
     * rewardsService.calculateRewards(user); }, executorService));
     * 
     * waitForAll(futures);
     * 
     * // WHEN List<UserReward> userRewards = user.getUserRewards();
     * System.out.println(userRewards.size()); // THEN
     * assertThat(userRewards.size())
     * .isEqualTo(gpsService.getAttractions().size()); }
     * 
     * public static <T> CompletableFuture<Void> waitForAll(
     * List<CompletableFuture<Void>> futures) { return CompletableFuture
     * .allOf(futures.toArray(new CompletableFuture[0])); }
     */
}
