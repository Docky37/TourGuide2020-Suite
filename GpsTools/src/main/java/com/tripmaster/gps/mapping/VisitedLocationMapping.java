package com.tripmaster.gps.mapping;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.tripmaster.gps.dto.LocationDTO;
import com.tripmaster.gps.dto.VisitedLocationDTO;

import gpsUtil.location.VisitedLocation;

@Component
public class VisitedLocationMapping {

    public VisitedLocationDTO mapToDTO(final VisitedLocation visitedLocation,
            final UUID userUUID) {
        VisitedLocationDTO visitedLocationDTO = new VisitedLocationDTO(
                new LocationDTO(visitedLocation.location.latitude,
                        visitedLocation.location.longitude),
                visitedLocation.timeVisited, userUUID);

        System.out.println(visitedLocationDTO.toString());
        return visitedLocationDTO;
    }

}
