package com.tripmaster.gps.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tripmaster.gps.dto.AttractionDTO;
import com.tripmaster.gps.dto.LocationDTO;
import com.tripmaster.gps.dto.VisitedLocationDTO;
import com.tripmaster.gps.mapping.AttractionMapping;
import com.tripmaster.gps.mapping.VisitedLocationMapping;
import com.tripmaster.gps.service.GpsService;

import gpsUtil.location.VisitedLocation;

@ExtendWith(SpringExtension.class)
public class GpsServiceTest {

    static {
        Locale.setDefault(Locale.US);
    }

    @Mock
    public AttractionMapping attractionMapping;

    @Mock
    public VisitedLocationMapping visitedLocationMapping;

    @InjectMocks
    IGpsService gpsService = new GpsService(visitedLocationMapping,
            attractionMapping);

    @Test
    public void givenAUserId_whenGetUserLocation_thenReturnAVisitedLocation() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        VisitedLocationDTO visitedLocationDTO = new VisitedLocationDTO(
                new LocationDTO(48.858482d, 2.294426d),
                new Date());
        given(visitedLocationMapping.mapToDTO(any(VisitedLocation.class)))
                .willReturn(visitedLocationDTO);
        // WHEN
        VisitedLocationDTO visitedLocation = gpsService.getUserLocation(userId);
        System.out.println(visitedLocation.getTimeVisited());
        System.out.println(visitedLocation.getLocation().getLatitude());
        System.out.println(visitedLocation.getLocation().getLongitude());
        // THEN
        assertThat(visitedLocation).isNotNull();
    }

    @Test
    public void whenGetAttractions_thenReturnANotEmptyAttractionList() {
        // WHEN
        List<AttractionDTO> attractions = gpsService.getAttractions();
        // THEN
        assertThat(attractions).size().isGreaterThanOrEqualTo(26);
    }

}
