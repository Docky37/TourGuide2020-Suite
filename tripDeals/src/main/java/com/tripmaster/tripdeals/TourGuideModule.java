package com.tripmaster.tripdeals;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tripPricer.TripPricer;

/**
 * This class defines the TripPricer bean candidate for Spring injection in
 * TipDealService.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Configuration
public class TourGuideModule {

    /**
     * Defines the TripPricer bean candidate.
     *
     * @return a WebClient
     */
    @Bean
    public TripPricer getTripPricer() {
        return new TripPricer();
    }

}
