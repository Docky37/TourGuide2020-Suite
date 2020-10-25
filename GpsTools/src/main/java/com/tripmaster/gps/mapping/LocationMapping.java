package com.tripmaster.gps.mapping;

import org.springframework.stereotype.Component;

import com.tripmaster.gps.dto.LocationDTO;

import gpsUtil.location.Location;

@Component
public class LocationMapping {

    public LocationDTO mapToDTO(final Location location) {
        LocationDTO locationDTO = new LocationDTO(location.latitude,
                location.longitude);
        System.out.println(locationDTO.toString());
        return locationDTO;
    }

}
