package com.tripmaster.gps.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@Service
public class GpsService implements IGpsService {

    public GpsUtil gpsUtil = new GpsUtil();

    public GpsService() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisitedLocation getUserLocation(UUID userId) {
        VisitedLocation visitedLocation = gpsUtil
                .getUserLocation(userId);
        return visitedLocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

}
