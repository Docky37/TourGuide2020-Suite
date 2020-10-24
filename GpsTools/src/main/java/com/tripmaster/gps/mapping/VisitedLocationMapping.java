package com.tripmaster.gps.mapping;

import com.tripmaster.gps.DTO.LocationDTO;
import com.tripmaster.gps.DTO.VisitedLocationDTO;

import gpsUtil.location.VisitedLocation;

public class VisitedLocationMapping {

    public VisitedLocationDTO mapToDTO(final VisitedLocation visitedLocation) {
        VisitedLocationDTO visitedLocationDTO = new VisitedLocationDTO(
                new LocationDTO(visitedLocation.location.latitude,
                        visitedLocation.location.longitude),
                        visitedLocation.timeVisited);

        System.out.println(visitedLocationDTO.toString());
        return visitedLocationDTO;
    }

}
