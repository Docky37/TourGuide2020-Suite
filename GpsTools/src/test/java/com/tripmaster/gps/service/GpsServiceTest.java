package com.tripmaster.gps.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.tripmaster.gps.service.GpsService;

import gpsUtil.location.VisitedLocation;
import gpsUtil.location.Attraction;

@SpringJUnitConfig(value = GpsService.class)
public class GpsServiceTest {

    static {
        Locale.setDefault(Locale.US);
    }

    @Autowired
    GpsService gpsService;

    @Test
    public void givenAUserId_whenGetUserLocation_thenReturnAVisitedLocation() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        // WHEN
        VisitedLocation visitedLocation = gpsService.getUserLocation(userId);
        System.out.println(visitedLocation.timeVisited);
        System.out.println(visitedLocation.location.latitude);
        System.out.println(visitedLocation.location.longitude);
        System.out.println(visitedLocation.userId);
        // THEN
        assertThat(visitedLocation).isNotNull();
    }

    @Test
    public void whenGetAttractions_thenReturnANotEmptyAttractionList() {
        // WHEN
        List<Attraction> attractions= gpsService.getAttractions();
        // THEN
        assertThat(attractions).size().isGreaterThanOrEqualTo(26);
    }


}
