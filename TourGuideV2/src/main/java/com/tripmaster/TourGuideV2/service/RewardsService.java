package com.tripmaster.TourGuideV2.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.UserReward;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;

/**
 * This service layer class is used to deal with the external RewardCentral.jar
 * library to manage user rewards.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Service
public class RewardsService implements IRewardsService {
    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    /**
     * Number of statute miles in one nautical mile.
     */
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    /**
     * Proximity buffer default value in miles.
     */
    private int defaultProximityBuffer = 10;
    /**
     * Current value of proximity buffer.
     */
    private int proximityBuffer = defaultProximityBuffer;
    /**
     * The maximum distance below which an attraction is close.
     */
    private int attractionProximityRange = 200;

    /**
     * A Rewards Webclient declaration. The bean is injected by Spring with the
     * class constructor @Autowired annotation.
     */
    WebClient webClientReward;

    /**
     * This class constructor allows Spring to inject one WebClient bean,
     * specified by the @Qualifier annotation (because there are 3 qualified
     * beans known by Spring context.
     *
     * @param gpsUtil
     * @param rewardCentral
     */
    @Autowired
    public RewardsService(
            @Qualifier("getWebClientReward") final WebClient pWebClientReward)
    {
        webClientReward = pWebClientReward;
    }

    /**
     * No parameters empty constructor.
     */
    RewardsService() {
    }

    /**
     * Special class constructor for unit test with a Spring injection of a
     * MockWebServer bean to mock the WebClient.
     *
     * @param testMode
     * @param pWebClientReward
     */
    public RewardsService(String testMode, WebClient pWebClientReward) {
        webClientReward = pWebClientReward;
    }

    /**
     * Setter of proximityBuffer.
     * 
     * @param proximityBuffer
     */
    @Override
    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    /**
     * Set proximityBuffer with default value.
     */
    @Override
    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    /**
     * Asynchronous method use to calculate user rewards.
     * 
     * @param user
     * @return a CompletableFuture<?>
     */
    @Override
    public CompletableFuture<?> calculateRewards(User user,
            List<Attraction> attractions) {

        return CompletableFuture.supplyAsync(() -> {
            user.getVisitedLocations().forEach(vl -> {
                // System.out.println("1. Method calculateRewards");
                attractions.stream()
                        .filter(a -> nearAttraction(vl, a))
                        .forEach(a -> {
                            if (user.getUserRewards().stream()
                                    .noneMatch(r -> r.attraction
                                            .getAttractionName()
                                            .equals(a.getAttractionName()))) {
                                // System.out.println("2. True");
                                // System.out.println(getRewardPoints(a, user));
                                user.addUserReward(new UserReward(vl, a,
                                        getRewardPoints(a, user)));
                            }
                        });

            });

            return user;
        });
    }

    /**
     * This method checks if the distance between location and attraction and
     * return false if the distance is greater than the attractionProximityRange
     * (and true if not).
     *
     * @param attraction
     * @param location
     * @return a boolean
     */
    @Override
    public boolean isWithinAttractionProximity(Attraction attraction,
            Location location) {
        logger.debug("isWithinAttractionProximity\n distance = "
                + getDistance(attraction, location));
        return !(getDistance(attraction, location) > attractionProximityRange);
    }

    /**
     * This method checks if the distance between visitedLocation and attraction
     * and return false if the distance is greater than the proximity buffer
     * (and true if not).
     *
     * @param visitedLocation
     * @param attraction
     * @return a boolean
     */
    private boolean nearAttraction(VisitedLocation visitedLocation,
            Attraction attraction) {
        logger.debug("nearAttraction - distance = "
                + getDistance(attraction, visitedLocation.getLocation()));

        return !(getDistance(attraction,
                visitedLocation.getLocation()) > proximityBuffer);
    }

    public double getDistance(Attraction attraction, Location location) {
        double distance = getDistance(new Location(attraction.getLatitude(),
                attraction.getLongitude()), location);
        return distance;
    }

    /**
     * This method builds and sends an HTTP request on localhost:8787/getReward
     * in order to get the attraction reward points for the given user.
     *
     * @param attraction
     * @param user
     * @return an int: the count of reward points
     */
    public int getRewardPoints(final Attraction attraction, final User user) {
        return webClientReward.get()
                .uri("/getReward?attractionId=" + attraction.getAttractionId()
                        + "&userId=" + user.getUserId())
                .retrieve()
                .bodyToMono(Integer.class).block();
    }

    /**
     * Calculation of the distance between two locations.
     *
     * @param loc1
     * @param loc2
     * @return a double: the distance in statute miles
     */
    @Override
    public double getDistance(final Location loc1, final Location loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math
                .acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
                        * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
