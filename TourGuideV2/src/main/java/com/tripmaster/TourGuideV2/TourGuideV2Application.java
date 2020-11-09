package com.tripmaster.TourGuideV2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class contains the main method used to run the application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@SpringBootApplication
public class TourGuideV2Application {

    /**
     * Main method that provides the entry point of this Spring application.
     *
     * @param args
     */
    public static void main(final String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(TourGuideV2Application.class, args);
    }

    /**
     * Empty class constructor.
     */
    protected TourGuideV2Application() {
    }

}
