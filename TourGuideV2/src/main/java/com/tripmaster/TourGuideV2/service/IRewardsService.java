package com.tripmaster.TourGuideV2.service;

import java.util.List;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;

import reactor.core.publisher.Mono;

public interface IRewardsService {

    /**
     * Setter of proximityBuffer.
     *
     * @param proximityBuffer
     */
    void setProximityBuffer(int proximityBuffer);

    /**
     * Set proximityBuffer with default value.
     */
    void setDefaultProximityBuffer();

    /**
     * Asynchronous method use to calculate user rewards.
     *
     * @param user
     * @param attractions
     */
    void calculateRewards(User user, List<Attraction> attractions);

    /**
     * This method checks if the distance between location and attraction and
     * return false if the distance is greater than the attractionProximityRange
     * (and true if not).
     *
     * @param attraction
     * @param location
     * @return a boolean
     */
    boolean isWithinAttractionProximity(Attraction attraction,
            Location location);

    /**
     * Calculation of the distance between two locations.
     *
     * @param loc1
     * @param loc2
     * @return a double: the distance in statute miles
     */
    double getDistance(Location loc1, Location loc2);

    /**
     * Get the distance between an attraction and a location.
     *
     * @param attraction
     * @param location
     * @return a double
     */
    double getDistance(Attraction attraction, Location location);

    /**
     * Get the reward points the user has wan for a visited attraction.
     *
     * @param visitedLocation
     * @param attraction
     * @param user
     * @return a Mono<Integer>
     */
    Mono<Integer> getRewardPoints(VisitedLocation visitedLocation,
            Attraction attraction, User user);

}
