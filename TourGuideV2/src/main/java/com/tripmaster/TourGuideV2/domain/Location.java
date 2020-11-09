package com.tripmaster.TourGuideV2.domain;

import java.util.UUID;

/**
 * Domain class used to store the data of gpsUtil Location class.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
public class Location {

    /**
     * The id (an UUID) of the User tracked at this Location.
     */
    private UUID userId;

    /**
     * The latitude of the Global Position location.
     */
    private double latitude;

    /**
     * The longitude of the Global Position location.
     */
    private double longitude;

    /**
     * Class constructor with 2 parameters.
     *
     * @param pLatitude
     * @param pLongitude
     */
    public Location(final double pLatitude, final double pLongitude) {
        userId = UUID.randomUUID();
        latitude = pLatitude;
        longitude = pLongitude;
    }

    /**
     * Class constructor with 3 parameters.
     *
     * @param pUserId
     * @param pLatitude
     * @param pLongitude
     */
    public Location(final UUID pUserId, final double pLatitude,
            final double pLongitude) {
        userId = pUserId;
        latitude = pLatitude;
        longitude = pLongitude;
    }

    /**
     * No argument and protected empty constructor.
     */
    protected Location() {
    }

    /**
     * Getter of latitude attribute.
     *
     * @return a double between -90 and +90
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter of longitude attribute.
     *
     * @return a double between -180 and +180
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "Location [latitude=" + latitude + ", longitude=" + longitude
                + "]";
    }

}
