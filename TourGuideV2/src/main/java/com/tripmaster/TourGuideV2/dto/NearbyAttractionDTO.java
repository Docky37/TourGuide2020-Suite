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
    private NearbyAttractionDTO() {
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
        this();
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
     * Getter of distance.
     *
     * @return a double, the distance userLocation attraction.location in miles
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Getter of userReward.
     *
     * @return a UserReward object
     */
    public int getUserReward() {
        return userReward;
    }

}
