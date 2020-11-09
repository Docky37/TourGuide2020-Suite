package com.tripmaster.gps.mapping;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.tripmaster.gps.dto.LocationDTO;
import com.tripmaster.gps.dto.VisitedLocationDTO;

import gpsUtil.location.VisitedLocation;

/**
 * VisitedLocationMapping class used to map GpsTools VisitedLocation to
 * VisitedLocationDTO.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Component
public class VisitedLocationMapping {

    /**
     * This method is used to map a VisitedLocation instance provided by GpsUtil
     * to a VisitedLocationDTO that will be returned to GpsController via
     * GpsService.
     *
     * @param userId
     * @param visitedLocation
     * @return a VisitedLocationDTO
     */
    public VisitedLocationDTO mapToDTO(final VisitedLocation visitedLocation,
            final UUID userId) {
        VisitedLocationDTO visitedLocationDTO = new VisitedLocationDTO(
                new LocationDTO(visitedLocation.location.latitude,
                        visitedLocation.location.longitude),
                visitedLocation.timeVisited, userId);

        System.out.println(visitedLocationDTO.toString());
        return visitedLocationDTO;
    }

}
