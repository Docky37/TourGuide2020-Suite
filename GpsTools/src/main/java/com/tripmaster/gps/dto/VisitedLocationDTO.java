package com.tripmaster.gps.dto;

import java.util.Date;
import java.util.UUID;

/**
 * DTO used to transfer the data of a gpsUtil VisitedLocation class, which is
 * the location where a user was at such time.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
public class VisitedLocationDTO {

    /**
     * The id of the owner of this visitedLocation.
     */
    private UUID userId;

    /**
     * This attribute tells us when this visitedLocation has been collected by
     * gpsUtil.
     */
    private Date timeVisited;

    /**
     * The location(latitude and longitude) of the VisitedLocation.
     *
     * @see LocationDTO
     */
    private LocationDTO location;

    /**
     * Full parameters class constructor.
     *
     * @param pTimeVisited
     * @param pLocation
     * @param pUserId
     */
    public VisitedLocationDTO(final LocationDTO pLocation,
            final Date pTimeVisited, final UUID pUserId) {
        this();
        timeVisited = (Date) pTimeVisited.clone();
        location = pLocation;
        userId = pUserId;
    }

    /**
     * No argument and private empty class constructor.
     */
    private VisitedLocationDTO() {
    }

    /**
     * The getter of the userId.
     *
     * @return an UUID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * The setter of the userId.
     *
     * @param pUserId
     */
    public void setUserId(final UUID pUserId) {
        this.userId = pUserId;
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
    public LocationDTO getLocation() {
        return location;
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "{location:" + location.toString()
                + ", timeVisited=" + timeVisited.clone() + ", userId:" + userId
                + "}";
    }

}
