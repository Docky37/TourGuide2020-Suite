package com.tripmaster.TourGuideV2.dto;

public class NearbyAttractionDTO {

    private LocationDTO attractionLocation;

    private double distance;

    private int userReward;

    /**
     * Empty class constructor
     */
    protected NearbyAttractionDTO() {
    }

    /**
     * All parameters class constructor.
     *
     * @param attractionLocation
     * @param distance
     * @param userReward
     */
    public NearbyAttractionDTO(LocationDTO attractionLocation, double distance,
            int userReward)
    {
        super();
        this.attractionLocation = attractionLocation;
        this.distance = distance;
        this.userReward = userReward;
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
     * @param attractionLocation
     */
    public void setAttractionLocation(LocationDTO attractionLocation) {
        this.attractionLocation = attractionLocation;
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
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
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
     * @param userReward
     */
    public void setUserReward(int userReward) {
        this.userReward = userReward;
    }

}
