package com.tripmaster.gps.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import dto.VisitedLocationDTO;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@SpringJUnitConfig(value = VisitedLocationMapping.class)
public class VisitedLocationMappingTest {

    static {
        Locale.setDefault(Locale.US);
    }

    @Autowired
    VisitedLocationMapping visitedLocationMapping;

    @Test
    public void givenAVisitedLocation_whenMapToDTO_thenReturnAVisitedLocationDTO() {
        // GIVEN
        VisitedLocation visitedLocation = new VisitedLocation(
                null, new Location(48.858482d, 2.294426d),
                new Date());
        ;
        // WHEN
        VisitedLocationDTO mappedVisitedLocation = visitedLocationMapping
                .mapToDTO(visitedLocation);
        // THEN
        assertThat(mappedVisitedLocation.getLocation().getLatitude())
                .isEqualTo(visitedLocation.location.latitude);
        assertThat(mappedVisitedLocation.getLocation().getLongitude())
                .isEqualTo(visitedLocation.location.longitude);
        assertThat(mappedVisitedLocation.getTimeVisited())
                .isEqualTo(visitedLocation.timeVisited);

    }

}
