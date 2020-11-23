package com.tripmaster.gps.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tripmaster.gps.dto.AttractionDTO;
import com.tripmaster.gps.dto.LocationDTO;
import com.tripmaster.gps.dto.VisitedLocationDTO;
import com.tripmaster.gps.mapping.AttractionMapping;
import com.tripmaster.gps.mapping.VisitedLocationMapping;
import com.tripmaster.gps.service.GpsService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import gpsUtil.location.Attraction;

@ExtendWith(SpringExtension.class)
public class GpsServiceTest {

    static {
        Locale.setDefault(Locale.US);
    }

    @Mock
    private AttractionMapping attractionMapping;

    @Mock
    private VisitedLocationMapping visitedLocationMapping;

    @Mock
    private GpsUtil gpsUtil;

    @InjectMocks
    GpsService gpsService = new GpsService(visitedLocationMapping,
            attractionMapping, gpsUtil);

    static List<Attraction> attractions = new ArrayList<>();
    static {
        Locale.setDefault(Locale.US);
        attractions.add(new Attraction("Tour Eiffel",
                "Paris", "France", 48.858482d, 2.294426d));
        attractions.add(new Attraction("Futuroscope",
                "Chasseneuil-du-Poitou", "France", 46.669752d, 0.368955d));
        attractions.add(new Attraction("Notre Dame",
                "Paris", "France", 48.853208d, 2.348640d));
        attractions.add(new Attraction("Musée Automobile",
                "Vernon", "France", 46.441387, 0.475771));
    }

    @Test
    public void givenAUserId_whenGetUserLocation_thenReturnAVisitedLocation() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        VisitedLocationDTO visitedLocationDTO = new VisitedLocationDTO(
                new LocationDTO(48.858482d, 2.294426d),
                new Date(), userId);
        given(gpsUtil.getUserLocation(userId)).willReturn(
                new VisitedLocation(userId, new Location(48.858482d, 2.294426d),
                        new Date()));

        given(visitedLocationMapping.mapToDTO(any(VisitedLocation.class),
                any(UUID.class))).willReturn(visitedLocationDTO);
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
        // GIVEN
        given(gpsUtil.getAttractions()).willReturn(attractions);
        // WHEN
        List<AttractionDTO> attractionDTO = gpsService.getAttractions();
        // THEN
        assertThat(attractionDTO).size().isEqualTo(4);
    }

}
