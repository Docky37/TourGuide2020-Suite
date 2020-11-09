package com.tripmaster.tripdeals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class contains the main method used to run the application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@SpringBootApplication
public class TripDealsApplication {

    /**
     * Main method that provides the entry point of this Spring application.
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(TripDealsApplication.class, args);
    }

    /**
     * Empty class constructor.
     */
    protected TripDealsApplication() {
    }

}
