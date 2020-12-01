package com.tripmaster.TourGuideV2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
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

    int visitedLocationsCount = 0;
    int rewardsCount = 0;

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
        InternalTestHelper.setInternalUserNumber(100000);
        TourGuideService tourGuideService = new TourGuideService(
                rewardsService, webClientTripDeals, webClientGps);

        tourGuideService.getTracker().stopTracking();
        List<User> allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> u.clearVisitedLocations());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        allUsers.forEach(u -> {
            tourGuideService.trackUserLocation(u).subscribe();
        });

        while (visitedLocationsCount < allUsers.size()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            visitedLocationsCount = 0;
            allUsers.forEach(u -> {
                if (u.getVisitedLocations().size() > 0) {
                    visitedLocationsCount++;
                };
            });
        }
        System.out.println(visitedLocationsCount
                + " visitedLocations... Ok now each user is localized!");

        stopWatch.stop();

        System.out.println("\n  *** highVolumeTrackLocation: Time Elapsed: "
                + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds.\n");
        assertThat(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS
                .toSeconds(stopWatch.getTime())).isTrue();
        allUsers.forEach(u -> {
            assertThat(u.getVisitedLocations().size()).isEqualTo(1);
        });

    }

    @Test
    public void highVolumeGetRewards() {
        // Users should be incremented up to 100,000, and test finishes within
        // 20 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        TourGuideService tourGuideService = new TourGuideService(
                rewardsService, webClientTripDeals, webClientGps);

        tourGuideService.getTracker().stopTracking();
        List<User> allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> {
            u.clearVisitedLocations();
            u.clearRewards();
        });

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Attraction> attractions = tourGuideService
                .getAllAttractionsFromGpsTools();
        Attraction attraction = attractions.get(0);

        allUsers.forEach(u -> u.addToVisitedLocations(
                new VisitedLocation(u.getUserId(),
                        new Location(attraction.getLatitude(),
                                attraction.getLongitude()),
                        new Date())));

        allUsers.forEach(u -> {
            rewardsService.calculateRewards(u, attractions);
        });

        while (rewardsCount < allUsers.size()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rewardsCount = 0;
            allUsers.forEach(u -> {
                if (u.getUserRewards().size() > 0) {
                    rewardsCount++;
                };
            });
        }
        System.out.println(rewardsCount
                + " rewards... Ok now each user has got his reward!");

        for (User user : allUsers) {
            assertThat(user.getUserRewards().size() > 0).isTrue();
        }
        stopWatch.stop();

        System.out.println("highVolumeGetRewards - Time Elapsed: "
                + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                + " seconds for " + allUsers.size());
        assertThat(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS
                .toSeconds(stopWatch.getTime())).isTrue();
    }

}
