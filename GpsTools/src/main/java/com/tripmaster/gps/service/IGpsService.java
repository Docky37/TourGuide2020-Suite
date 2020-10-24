package com.tripmaster.gps.service;

import java.util.List;
import java.util.UUID;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

/**
 * This interface provides 2 methods to deal with the gpsUtil.jar library.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
public interface IGpsService {

    /**
     * Method that calls the gpsUti.jar to get the location of a user by his id.
     *
     * @param uuid
     * @return a VisitedLocation
     */
    VisitedLocation getUserLocation(UUID uuid);

    /**
     * Method that calls the gpsUti.jar library to get the List of all
     * Attractions.
     *
     * @return an Attraction list
     */
    List<Attraction> getAttractions();

}
