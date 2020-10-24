package com.tripmaster.gps.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import dto.LocationDTO;
import gpsUtil.location.Location;

@SpringJUnitConfig(value = LocationMapping.class)
public class LocationMappingTest {

    static {
        Locale.setDefault(Locale.US);
    }

    @Autowired
    LocationMapping locationMapping;

    @Test
    public void givenALocation_whenMapToDTO_thenReturnALocationDTO() {
        // GIVEN
        Location location = new Location(48.858482d, 2.294426d);
        // WHEN
        LocationDTO mappedLocation = locationMapping.mapToDTO(location);
        // THEN
        assertThat(mappedLocation.getLatitude())
                .isEqualTo(location.latitude);
        assertThat(mappedLocation.getLongitude())
                .isEqualTo(location.longitude);

    }

}
