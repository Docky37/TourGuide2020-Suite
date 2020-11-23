package com.tripmaster.TourGuideV2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class only used in "docker" profile defines 3 WebClient beans that
 * allows TourGuide to send requests to the others micro-services when they are
 * all embedded in Dockers containers in the TourGuide2020-Suite network. In
 * this case, tourguide container communicates with gps, rewards and tripdeals
 * containers via their hostnames (which are container names).
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Configuration
@Profile("docker")
public class TourGuideV2DockerImageBuild {

    /**
     * Defines the WebClient beans to deal with TripDeals API.
     *
     * @return a WebClient
     */
    @Bean
    public WebClient getWebClientTripDeals() {
        return WebClient.create("http://tripdeals:8888"); // for Docker
    }

    /**
     * Defines the WebClient beans to deal with GpsTools API.
     *
     * @return a WebClient
     */
    @Bean
    public WebClient getWebClientGps() {
        return WebClient.create("http://gps:8889");
    }

    /**
     * Defines the WebClient beans to deal with GetRewards API.
     *
     * @return a WebClient
     */
    @Bean
    public WebClient getWebClientReward() {
        return WebClient.create("http://rewards:8787");
    }
}
