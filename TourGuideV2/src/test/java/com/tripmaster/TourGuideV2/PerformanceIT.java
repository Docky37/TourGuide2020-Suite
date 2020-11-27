package com.tripmaster.TourGuideV2;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;
import com.tripmaster.TourGuideV2.helper.InternalTestHelper;
import com.tripmaster.TourGuideV2.service.IRewardsService;
import com.tripmaster.TourGuideV2.service.RewardsService;
import com.tripmaster.TourGuideV2.service.TourGuideService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class PerformanceIT {

    IRewardsService rewardsService = new RewardsService(
            WebClient.create("http://localhost:8787"));

    WebClient webClientTripDeals = WebClient.create("http://localhost:8888");

    WebClient webClientGps = WebClient.create("http://localhost:8889");;

    int totalRewards = 0;

    int count = 0;
    /*
     * A note on performance improvements:
     * 
     * The number of users generated for the high volume tests can be easily
     * adjusted via this method:
     * 
     * InternalTestHelper.setInternalUserNumber(100000);
     * 
     * 
     * These tests can be modified to suit new solutions, just as long as the
     * performance metrics at the end of the tests remains consistent.
     * 
     * These are performance metrics that we are trying to hit:
     * 
     * highVolumeTrackLocation: 100,000 users within 15 minutes:
     * assertTrue(TimeUnit.MINUTES.toSeconds(15) >=
     * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     * highVolumeGetRewards: 100,000 users within 20 minutes:
     * assertTrue(TimeUnit.MINUTES.toSeconds(20) >=
     * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */

    @Test
    public void highVolumeTrackLocation() throws InterruptedException {

        // Users should be incremented up to 100,000, and test finishes within
        // 15 minutes
        InternalTestHelper.setInternalUserNumber(100);
        TourGuideService tourGuideService = new TourGuideService(
                rewardsService, webClientTripDeals, webClientGps);
        tourGuideService.getTracker().stopTracking();

        List<User> allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u-> u.clearVisitedLocations());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        allUsers.forEach(u -> executorService.submit(() -> {
            CompletableFuture<?> result = tourGuideService.trackUserLocation(u);
            assertThat(result).isNotNull()
                    .isInstanceOf(CompletableFuture.class);
            try {
                assertThat(result.get()).isInstanceOf(VisitedLocationDTO.class);
                System.out.println(
                        u.getUserName() + " " + result.get().toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }));

        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        stopWatch.stop();
        tourGuideService.getTracker().stopTracking();

        System.out.println("highVolumeTrackLocation: Time Elapsed: "
                + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds.");
        assertThat(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS
                .toSeconds(stopWatch.getTime())).isTrue();
        allUsers.forEach(u -> {
            assertThat(u.getVisitedLocations().size()).isEqualTo(1);
        });
        System.out.println("final count -> " + count);

    }

    @Test
    public void highVolumeGetRewards() {

        // Users should be incremented up to 100,000, and test finishes within
        // 20 minutes
        InternalTestHelper.setInternalUserNumber(1000);
        TourGuideService tourGuideService = new TourGuideService(
                rewardsService, webClientTripDeals, webClientGps);
        tourGuideService.getTracker().stopTracking();
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Attraction> attractions = tourGuideService
                .getAllAttractionsFromGpsTools();
        Attraction attraction = attractions.get(0);
        List<User> allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> u.addToVisitedLocations(
                new VisitedLocation(u.getUserId(),
                        new Location(attraction.getLatitude(),
                                attraction.getLongitude()),
                        new Date())));

        allUsers.forEach(u -> executorService.submit(() -> {
            CompletableFuture<?> result = rewardsService.calculateRewards(u,
                    attractions);
            assertThat(result).isNotNull()
                    .isInstanceOf(CompletableFuture.class);
            try {
                assertThat(result.get()).isInstanceOf(User.class);
                System.out.println(
                        u.getUserName() + " " + result.get().toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            ;
        }));
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(19, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        for (User user : allUsers) {
            assertThat(user.getUserRewards().size() > 0).isTrue();
        }
        executorService.shutdown();
        stopWatch.stop();

        System.out.println("highVolumeGetRewards: Time Elapsed: "
                + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds.");
        assertThat(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS
                .toSeconds(stopWatch.getTime())).isTrue();
        System.out.println(allUsers.size());
        allUsers.forEach(u -> totalRewards += u.getUserRewards().size());
        System.out.println(totalRewards);

    }

}
