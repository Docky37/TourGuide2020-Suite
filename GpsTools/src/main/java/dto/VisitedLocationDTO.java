package dto;

import java.util.Date;

/**
 * DTO used to transfer the data of a gpsUtil VisitedLocation class, witch is
 * the location where a user was at such time.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
public class VisitedLocationDTO {

    /**
     * This attribute tells us when this visitedLocation has been collected by
     * gpsUtil.
     */
    private Date timeVisited;

    /**
     * The location(latitude and longitude) of the VisitedLocation.
     */
    private LocationDTO location;

    /**
     * Full parameters class constructor.
     *
     * @param pTimeVisited
     * @param pLocation
     */
    public VisitedLocationDTO(final LocationDTO pLocation,
            final Date pTimeVisited)
    {
        timeVisited = pTimeVisited;
        location = pLocation;
    }

    /**
     * No argument and protected empty class constructor.
     */
    protected VisitedLocationDTO() {
    }

    /**
     * Getter of timeVisited attribute.
     *
     * @return a LocalDateTime
     */
    public Date getTimeVisited() {
        return timeVisited;
    }

    /**
     * Getter of timeVisited attribute.
     *
     * @return a LocationDTO
     */
    public LocationDTO getLocation() {
        return location;
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "VisitedLocationDTO [location=" + location.toString()
                + ", timeVisited=" + timeVisited + "]";
    }

}
