package com.tripmaster.gps.mapping;

import dto.AttractionDTO;
import gpsUtil.location.Attraction;

public class AttractionMapping {

    public AttractionDTO mapToDTO(final Attraction attraction) {
        AttractionDTO attractionDTO = new AttractionDTO(
                attraction.attractionName, attraction.city, attraction.state,
                attraction.latitude, attraction.longitude);
        System.out.println(attractionDTO.toString());
        return attractionDTO;
    }

}
