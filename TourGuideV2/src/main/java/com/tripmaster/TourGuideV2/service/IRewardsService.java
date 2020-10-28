package com.tripmaster.TourGuideV2.service;

import java.util.List;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.domain.User;

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
     * @return a CompletableFuture<?>
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
    
    double getDistance(Attraction attraction, Location location);

    int getRewardPoints(Attraction a, User user);

}