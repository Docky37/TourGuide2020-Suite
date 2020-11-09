package com.tripmaster.TourGuideV2.domain;

import java.util.UUID;

/**
 * DTO used to transfer the data of gpsUtil Attraction class.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
public class Attraction {

    /**
     * The id (an UUID) of the attraction.
     */
    private UUID attractionId;

    /**
     * The name of this attraction.
     */
    private String attractionName;

    /**
     * The city where this attraction is located in.
     */
    private String city;

    /**
     * The location's latitude of this attraction.
     */
    private double latitude;

    /**
     * The location's longitude of this attraction.
     */
    private double longitude;

    /**
     * The state where this attraction is located in.
     */
    private String state;

    /**
     * Full parameters class constructor.
     *
     * @param pAttractionId
     * @param pAttractionName
     * @param pCity
     * @param pLatitude
     * @param pLongitude
     * @param pState
     */
    public Attraction(final UUID pAttractionId, final String pAttractionName,
            final String pCity, final String pState,
            final double pLatitude, final double pLongitude) {
        attractionId = pAttractionId;
        attractionName = pAttractionName;
        city = pCity;
        state = pState;
        latitude = pLatitude;
        longitude = pLongitude;
    }

    /**
     * No argument and protected empty class constructor.
     */
    protected Attraction() {
    }

    /**
     * Getter of the attractionId attribute.
     *
     * @return a String
     */
    public UUID getAttractionId() {
        return attractionId;
    }

    /**
     * Getter of the attractionName attribute.
     *
     * @return a String
     */
    public String getAttractionName() {
        return attractionName;
    }

    /**
     * Getter of the city attribute.
     *
     * @return a String
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter of the state attribute.
     *
     * @return a String
     */
    public String getState() {
        return state;
    }

    /**
     * Getter of the location's latitude.
     *
     * @return a double
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter of the location's longitude.
     *
     * @return a double
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "{\"attractionName\":\"" + attractionName + "\",\"city\":\""
                + city + "\", \"state\":\"" + state + "\", \"latitude\":"
                + latitude + ", \"longitude\":" + longitude + "}";
    }

}
