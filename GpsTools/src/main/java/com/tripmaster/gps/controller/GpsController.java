package com.tripmaster.gps.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripmaster.gps.dto.AttractionDTO;
import com.tripmaster.gps.dto.VisitedLocationDTO;
import com.tripmaster.gps.service.IGpsService;

/**
 * This GpsController class exposes two end-points. The first one is used to get
 * userLocation by his userId, the second provides the list of all attractions.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@RestController
public class GpsController {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    /**
     * GpsService instance declaration, the bean is injected by Spring when
     * GpsController is created.
     */
    @Autowired
    private IGpsService gpsService;

    /**
     * HTML GET request used to get the location of the user by his userId.
     *
     * @param userId
     * @return a VisitedLocationDTO (with timeVisited an locationDTO attributes)
     * @see LocationDTO
     */
    @GetMapping("/getUserLocation")
    public VisitedLocationDTO getUserLocation(@RequestParam final UUID userId) {
        logger.info("New HTML GET Request: /getLocation?userId= {}", userId);
        VisitedLocationDTO visitedLocation = gpsService.getUserLocation(userId);
        logger.info(" -> visitedLocation = {}", visitedLocation.toString());
        return visitedLocation;
    }

    /**
     * HTML GET request used to get the list of all attractions referenced by
     * the GpsUtil application.
     *
     * @return a List<AttractionDTO>
     * @see AttractionDTO
     */
    @GetMapping("/getAllAttractions")
    public List<AttractionDTO> getAllAttractions() {
        logger.info("New HTML GET Request: /getAllAttractions");
        List<AttractionDTO> attractions = gpsService.getAttractions();
        attractions.stream().forEach(a -> logger.info(a.toString()));
        return attractions;
    }
}
