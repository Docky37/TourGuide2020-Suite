package com.tripmaster.gps.mapping;

import dto.LocationDTO;
import dto.VisitedLocationDTO;
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
