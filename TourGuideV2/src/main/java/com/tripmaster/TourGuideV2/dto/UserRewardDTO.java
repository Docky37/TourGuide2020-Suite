package com.tripmaster.TourGuideV2.dto;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;

/**
 * This class is used to store the details of a reward.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class UserRewardDTO {

    /**
     * The visitedLocation that gives this reward.
     */
    private final VisitedLocation visitedLocation;

    /**
     * The attraction concerned by this reward.
     */
    private final Attraction attraction;

    /**
     * The number of points of this reward.
     */
    private int rewardPoints;

    /**
     * All parameters class constructor.
     *
     * @param pVisitedLocation
     * @param pAttraction
     * @param pRewardPoints
     */
    public UserRewardDTO(final VisitedLocation pVisitedLocation,
            final Attraction pAttraction, final int pRewardPoints) {
        visitedLocation = pVisitedLocation;
        attraction = pAttraction;
        rewardPoints = pRewardPoints;
    }

    /**
     * Class constructor without rewardPoints parameter.
     *
     * @param pVisitedLocation
     * @param pAttraction
     */
    public UserRewardDTO(final VisitedLocation pVisitedLocation,
            final Attraction pAttraction) {
        visitedLocation = pVisitedLocation;
        attraction = pAttraction;
    }

    /**
     * Setter of rewardPoints.
     *
     * @param pRewardPoints
     */
    public void setRewardPoints(final int pRewardPoints) {
        rewardPoints = pRewardPoints;
    }

    /**
     * Getter of rewardPoints.
     *
     * @return a int, the number of reward points.
     */
    public int getRewardPoints() {
        return rewardPoints;
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "UserReward [visitedLocation=" + visitedLocation
                + ", attraction=" + attraction + ", rewardPoints="
                + rewardPoints + "]";
    }

}
