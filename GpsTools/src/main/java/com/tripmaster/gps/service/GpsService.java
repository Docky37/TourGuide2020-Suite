package com.tripmaster.gps.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripmaster.gps.dto.AttractionDTO;
import com.tripmaster.gps.dto.VisitedLocationDTO;
import com.tripmaster.gps.mapping.AttractionMapping;
import com.tripmaster.gps.mapping.VisitedLocationMapping;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;

@Service
public class GpsService implements IGpsService {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(GpsService.class);

    /**
     * Create an instance of GpsUtil.
     */
    @Autowired
    private GpsUtil gpsUtil;

    /**
     * Declaration of a AttractionMapping instance that will be injected by
     * Spring.
     */
    @Autowired
    private AttractionMapping attractionMapping;

    /**
     * Declaration of a VisitedLocationMapping instance that will be injected by
     * Spring.
     */
    @Autowired
    private VisitedLocationMapping visitedLocationMapping;

    /**
     * No argument empty constructor.
     */
    private GpsService() {
    }

    /**
     * Class constructor used to inject Mock beans for tests.
     *
     * @param pVisitedLocationMapping
     * @param pAttractionMapping
     * @param pGpsUtil
     */
    public GpsService(final VisitedLocationMapping pVisitedLocationMapping,
            final AttractionMapping pAttractionMapping,
            final GpsUtil pGpsUtil) {
        this();
        visitedLocationMapping = pVisitedLocationMapping;
        attractionMapping = pAttractionMapping;
        gpsUtil = pGpsUtil;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisitedLocationDTO getUserLocation(final UUID userId) {

        VisitedLocationDTO visitedLocationDTO = visitedLocationMapping
                .mapToDTO(gpsUtil.getUserLocation(userId), userId);
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
        attractions.forEach(a -> {
            attractionsDTO.add(attractionMapping.mapToDTO(a));
        });
        logger.debug(attractionsDTO.toString());
        return attractionsDTO;
    }

}
