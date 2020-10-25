package com.tripmaster.gps.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripmaster.gps.mapping.AttractionMapping;
import com.tripmaster.gps.mapping.VisitedLocationMapping;

import dto.AttractionDTO;
import dto.VisitedLocationDTO;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;

@Service
public class GpsService implements IGpsService {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(GpsService.class);

    /**
     * 
     */
    public GpsUtil gpsUtil = new GpsUtil();

    /**
     * 
     */
    @Autowired
    public AttractionMapping attractionMapping;

    /**
     * 
     */
    @Autowired
    public VisitedLocationMapping visitedLocationMapping;

    /**
     * No argument empty constructor.
     */
    public GpsService() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisitedLocationDTO getUserLocation(UUID userId) {

        VisitedLocationDTO visitedLocationDTO = visitedLocationMapping
                .mapToDTO(gpsUtil.getUserLocation(userId));
        logger.debug(visitedLocationDTO.toString());
        return visitedLocationDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AttractionDTO> getAttractions() {
        List<Attraction> attractions = gpsUtil.getAttractions();
        List<AttractionDTO> attractionsDTO = new ArrayList<>();
        attractions.forEach(a->{
            attractionsDTO.add(attractionMapping.mapToDTO(a));
        });
        logger.debug(attractionsDTO.toString());
        return attractionsDTO;
    }

}
