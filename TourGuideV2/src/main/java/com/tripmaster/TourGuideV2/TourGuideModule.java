package com.tripmaster.TourGuideV2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class defines 3 WebClient beans that allows TourGuide to send requests
 * to the other application of TourGuide2020-Suite.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Configuration
public class TourGuideModule {

    /**
     * Defines the WebClientTripdeals beans to deal with TripDeals API for dev
     * profile.
     *
     * @return a WebClient
     */
    @Bean
    @Profile("dev")
    public WebClient getWebClientTripDeals() {
        return WebClient.create("http://127.0.0.1:8888");
    }

    /**
     * Defines the WebClientTripdeals beans to deal with TripDeals API for
     * docker profile.
     *
     * @return a WebClient
     */
    @Bean
    @Profile("docker")
    public WebClient getDockerWebClientTripDeals() {
        return WebClient.create("http://tripdeals:8888");
    }

    /**
     * Defines the WebClient beans to deal with GpsTools API.
     *
     * @return a WebClient
     */
    @Bean
    @Profile("dev")
    public WebClient getWebClientGps() {
        return WebClient.create("http://127.0.0.1:8889");
    }

    /**
     * Defines the WebClient beans to deal with GpsTools API.
     *
     * @return a WebClient
     */
    @Bean
    @Profile("docker")
    public WebClient getDockerWebClientGps() {
        return WebClient.create("http://gps:8889");
    }

    /**
     * Defines the WebClient beans to deal with GetRewards API.
     *
     * @return a WebClient
     */
    @Bean
    @Profile("dev")
    public WebClient getWebClientReward() {
        return WebClient.create("http://127.0.0.1:8787");
    }

    /**
     * Defines the WebClient beans to deal with GetRewards API.
     *
     * @return a WebClient
     */
    @Bean
    @Profile("docker")
    public WebClient getDockerWebClientReward() {
        return WebClient.create("http://rewards:8787");
    }

}
