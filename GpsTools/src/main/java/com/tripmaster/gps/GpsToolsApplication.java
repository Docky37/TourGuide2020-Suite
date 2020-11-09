package com.tripmaster.gps;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class contains the main method used to run the application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@SpringBootApplication
public class GpsToolsApplication {

    /**
     * Main method that provides the entry point of this Spring application.
     *
     * @param args
     */

    public static void main(final String[] args) {
        Locale.setDefault(Locale.US);
        SpringApplication.run(GpsToolsApplication.class, args);
    }

    /**
     * No argument empty class constructor.
     */
    protected GpsToolsApplication() {
    }

}
