package com.tripmaster.TourGuideV2.domain;

import java.util.Date;
import java.util.UUID;

/**
 * DTO used to transfer the data of a gpsUtil VisitedLocation class, witch is
 * the location where a user was at such time.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
public class VisitedLocation {

    /**
     * The id attribute of the user.
     */
    private UUID userId;

    /**
     * This attribute tells us when this visitedLocation has been collected by
     * gpsUtil.
     */
    private Date timeVisited;

    /**
     * The location(latitude and longitude) of the VisitedLocation.
     */
    private Location location;

    /**
     * Class constructor with 2 parameters..
     *
     * @param pTimeVisited
     * @param pLocation
     */
    public VisitedLocation(final Location pLocation, final Date pTimeVisited) {
        userId = UUID.randomUUID();
        timeVisited = (Date) pTimeVisited.clone();
        location = pLocation;
    }

    /**
     * No argument and protected empty class constructor.
     */
    protected VisitedLocation() {
    }

    /**
     * Full parameters class constructor.
     *
     * @param pUserId
     * @param pLocation
     * @param pTimeVisited
     */
    public VisitedLocation(final UUID pUserId, final Location pLocation,
            final Date pTimeVisited) {
        userId = pUserId;
        timeVisited = (Date) pTimeVisited.clone();
        location = pLocation;
    }

    /**
     * Getter of userId.
     *
     * @return an UUID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Getter of timeVisited attribute.
     *
     * @return a LocalDateTime
     */
    public Date getTimeVisited() {
        return (Date) timeVisited.clone();
    }

    /**
     * Getter of timeVisited attribute.
     *
     * @return a LocationDTO
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "VisitedLocation [location=" + location.toString()
                + ", timeVisited=" + timeVisited + "]";
    }

}
