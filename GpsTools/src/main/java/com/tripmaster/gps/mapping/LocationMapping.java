package com.tripmaster.gps.mapping;

import dto.LocationDTO;
import gpsUtil.location.Location;

public class LocationMapping {

    public LocationDTO mapToDTO(final Location location) {
        LocationDTO locationDTO = new LocationDTO(location.latitude,
                location.longitude);
        System.out.println(locationDTO.toString());
        return locationDTO;
    }

}
