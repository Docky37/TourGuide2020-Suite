package com.tripmaster.gps.mapping;

import org.springframework.stereotype.Component;

import com.tripmaster.gps.dto.AttractionDTO;

import gpsUtil.location.Attraction;

/**
 * AttractionMapping class used to map GpsTools Attraction to AttractionDTO.
 *
 * @author Thierry SCHREINER
 * @version 1.0
 * @since October 2020
 */
@Component
public class AttractionMapping {

    /**
     * This method is used to map a Attraction instance provided by GpsUtil to a
     * AttractionDTO that will be returned to GpsController via GpsService.
     *
     * @param attraction
     * @return an AttractionDTO
     */
    public AttractionDTO mapToDTO(final Attraction attraction) {
        AttractionDTO attractionDTO = new AttractionDTO(
                attraction.attractionName, attraction.city, attraction.state,
                attraction.latitude, attraction.longitude);
        System.out.println(attractionDTO.toString());
        return attractionDTO;
    }

}
