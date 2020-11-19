package com.tripmaster.gps;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;

/**
 * This class defines a GpsUtil bean that allows GpsService to send requests
 * to the gpsUtil.jar application of TourGuide2020-Suite.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Configuration
public class TourGuideModule {

    /**
     * Defines the GpsUtil bean.
     *
     * @return a GpsUtil
     */
    @Bean
    public GpsUtil getGpsUtil() {
        return new GpsUtil();
    }

}
