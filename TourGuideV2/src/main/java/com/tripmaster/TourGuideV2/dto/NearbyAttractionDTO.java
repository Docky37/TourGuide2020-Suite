package com.tripmaster.TourGuideV2.dto;

/**
 * This Data Transfer Object is used as answer to getNearbyAttractions request.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class NearbyAttractionDTO {

    /**
     * This attribute store the location of the attraction.
     *
     * @see LocationDTO
     */
    private LocationDTO attractionLocation;

    /**
     * This attribute gives us the distance between the attraction and the
     * current user's location.
     */
    private double distance;

    /**
     * This attribute tells us the reward user will get by visiting this
     * attraction.
     */
    private int userReward;

    /**
     * Empty class constructor.
     */
    protected NearbyAttractionDTO() {
    }

    /**
     * All parameters class constructor.
     *
     * @param pAttractionLocation
     * @param pDistance
     * @param pUserReward
     */
    public NearbyAttractionDTO(final LocationDTO pAttractionLocation,
            final double pDistance, final int pUserReward) {
        attractionLocation = pAttractionLocation;
        distance = pDistance;
        userReward = pUserReward;
    }

    /**
     * Getter of attractionLocation.
     *
     * @return a Location object (latitude and longitude of attraction)
     */
    public LocationDTO getAttractionLocation() {
        return attractionLocation;
    }

    /**
     * Setter of attractionLocation.
     *
     * @param pAttractionLocation
     */
    public void setAttractionLocation(final LocationDTO pAttractionLocation) {
        this.attractionLocation = pAttractionLocation;
    }

    /**
     * Getter of distance.
     *
     * @return a double, the distance userLocation attraction.location in miles
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Setter of distance.
     *
     * @param pDistance
     */
    public void setDistance(final double pDistance) {
        distance = pDistance;
    }

    /**
     * Getter of userReward.
     *
     * @return a UserReward object
     */
    public int getUserReward() {
        return userReward;
    }

    /**
     * Setter of userReward.
     *
     * @param pUserReward
     */
    public void setUserReward(final int pUserReward) {
        userReward = pUserReward;
    }

}
