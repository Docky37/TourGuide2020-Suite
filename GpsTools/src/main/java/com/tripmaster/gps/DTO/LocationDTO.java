package com.tripmaster.gps.DTO;

/**
 * DTO used to transfer the data of gpsUtil Location class.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
public class LocationDTO {

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
    public LocationDTO(final double pLatitude, final double pLongitude) {
        latitude = pLatitude;
        longitude = pLongitude;
    }

    /**
     * No argument and protected empty  constructor.
     */
    protected LocationDTO() {
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
        return "LocationDTO [latitude=" + latitude + ", longitude=" + longitude
                + "]";
    }

}
