package com.tripmaster.gps.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import dto.AttractionDTO;
import gpsUtil.location.Attraction;

@SpringJUnitConfig(value = AttractionMapping.class)
public class AttractionMappingTest {

    static {
        Locale.setDefault(Locale.US);
    }

    @Autowired
    AttractionMapping attractionMapping;

    @Test
    public void givenAnAttraction_whenMapToDTO_thenReturnAnAttractionDTO() {
        // GIVEN
        Attraction attraction = new Attraction("Tour Eiffel", "Paris",
                "France", 48.858482d, 2.294426d);
        // WHEN
        AttractionDTO mappedAttraction = attractionMapping.mapToDTO(attraction);
        // THEN
        assertThat(mappedAttraction.getAttractionName())
                .isEqualTo(attraction.attractionName);
        assertThat(mappedAttraction.getCity()).isEqualTo(attraction.city);
        assertThat(mappedAttraction.getState()).isEqualTo(attraction.state);
        assertThat(mappedAttraction.getLatitude())
                .isEqualTo(attraction.latitude);
        assertThat(mappedAttraction.getLongitude())
                .isEqualTo(attraction.longitude);

    }

}
