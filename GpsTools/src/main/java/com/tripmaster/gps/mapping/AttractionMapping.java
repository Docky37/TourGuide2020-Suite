package com.tripmaster.gps.mapping;

import org.springframework.stereotype.Component;

import com.tripmaster.gps.dto.AttractionDTO;

import gpsUtil.location.Attraction;

@Component
public class AttractionMapping {

    public AttractionDTO mapToDTO(final Attraction attraction) {
        AttractionDTO attractionDTO = new AttractionDTO(
                attraction.attractionName, attraction.city, attraction.state,
                attraction.latitude, attraction.longitude);
        System.out.println(attractionDTO.toString());
        return attractionDTO;
    }

}
