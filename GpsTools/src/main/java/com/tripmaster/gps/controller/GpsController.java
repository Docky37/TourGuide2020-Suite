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
import com.tripmaster.gps.service.GpsService;

@RestController
public class GpsController {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(GpsController.class);
    
    @Autowired
    GpsService gpsService;
    
    /**
     * HTML GET request used to get the location of the user by his userId.
     * @param userId
     * @return a VisitedLocationDTO
     */
    @GetMapping("/getLocation")
    public VisitedLocationDTO getUserLocation(@RequestParam UUID userId) {
        logger.info("New HTML GET Request: /getLocation?userId=?",userId);
        VisitedLocationDTO visitedLocation = gpsService.getUserLocation(userId);
        logger.info(visitedLocation.toString());
        return visitedLocation;
    }
    
    @GetMapping("/getAllAttractions")
    public List<AttractionDTO> getAllAttractions(){
        logger.info("New HTML GET Request: /getLocation");
        List<AttractionDTO> attractions = gpsService.getAttractions();
        attractions.stream().forEach(a->logger.info(a.toString()));
        return attractions;
    }
}
