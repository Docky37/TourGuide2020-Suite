package com.tripmaster.gps.mapping;

import org.springframework.stereotype.Component;

import com.tripmaster.gps.dto.LocationDTO;

import gpsUtil.location.Location;

/**
 * LocationMapping class used to map GpsTools Location to LocationDTO.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Component
public class LocationMapping {

    /**
     * This method is used to map a Location instance provided by GpsUtil to a
     * LocationDTO that will be returned to GpsController via GpsService.
     *
     * @param location
     * @return a LocationDTO
     */
    public LocationDTO mapToDTO(final Location location) {
        LocationDTO locationDTO = new LocationDTO(location.latitude,
                location.longitude);
        System.out.println(locationDTO.toString());
        return locationDTO;
    }

}
