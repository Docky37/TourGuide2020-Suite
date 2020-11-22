package com.tripmaster.TourGuideV2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class used in dev profile defines 3 WebClient beans that allows
 * TourGuide to send requests to the other application of TourGuide2020-Suite.
 * This configuration allows to makes the 4 micro-services to run and
 * communicate in your IDE.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Configuration
@Profile("dev")
public class TourGuideModule {

    /**
     * Defines the WebClient beans to deal with TripDeals API.
     *
     * @return a WebClient
     */
    @Bean
    public WebClient getWebClientTripDeals() {
        return WebClient.create("http://127.0.0.1:8888"); // run from IDE
    }

    /**
     * Defines the WebClient beans to deal with GpsTools API.
     *
     * @return a WebClient
     */
    @Bean
    public WebClient getWebClientGps() {
        return WebClient.create("http://127.0.0.1:8889");
    }

    /**
     * Defines the WebClient beans to deal with GetRewards API.
     *
     * @return a WebClient
     */
    @Bean
    public WebClient getWebClientReward() {
        return WebClient.create("http://127.0.0.1:8787");
    }
}
