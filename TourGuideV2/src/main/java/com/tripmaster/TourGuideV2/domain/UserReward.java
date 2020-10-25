package com.tripmaster.TourGuideV2.domain;


/**
 * This class is used to store the details of a reward.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class UserReward {

    /**
     * The visitedLocation that gives this reward.
     */
    public final VisitedLocation visitedLocation;

    /**
     * The attraction concerned by this reward.
     */
    public final Attraction attraction;

    /**
     * The number of points of this reward.
     */
    private int rewardPoints;

    /**
     * All parameters class constructor.
     *
     * @param visitedLocation
     * @param attraction
     * @param rewardPoints
     */
    public UserReward(VisitedLocation visitedLocation, Attraction attraction,
            int rewardPoints)
    {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }

    /**
     * Class constructor without rewardPoints parameter.
     *
     * @param visitedLocation
     * @param attraction
     */
    public UserReward(VisitedLocation visitedLocation, Attraction attraction) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
    }

    /**
     * Setter of rewardPoints.
     *
     * @param rewardPoints
     */
    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
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
