package com.tripmaster.TourGuideV2.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.time.StopWatch;

import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.service.ITourGuideService;

/**
 * This class in charge of regularly launch the tracking of all users location.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Component
public class Tracker extends Thread {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    /**
     * Defines the interval in minutes between to calls for users tracking.
     */
    private static final long TRACKING_POLLING_INTERVAL = TimeUnit.MINUTES
            .toSeconds(5);
    /**
     * Create an instance of a single thread ExecutorService.
     */
    private final ExecutorService executorService = Executors
            .newSingleThreadExecutor();
    /**
     * TourGuideService instance declaration.
     */
    private final ITourGuideService tourGuideService;
    /**
     * Declares and initializes to false a boolean, that indicates if tracker is
     * stopped (true) or run (false).
     */
    private boolean isTrackerStopped = false;

    /**
     * Class constructor.
     *
     * @param pTourGuideService
     */
    public Tracker(final ITourGuideService pTourGuideService) {
        this.tourGuideService = pTourGuideService;
        executorService.submit(this);
    }

    /**
     * Assures to shut down the Tracker thread.
     */
    public void stopTracking() {
        isTrackerStopped = true;
        executorService.shutdownNow();
    }

    /**
     * Overridden Run super method.
     */
    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        while (true) {
            if (Thread.currentThread().isInterrupted() || isTrackerStopped) {
                logger.debug("Tracker stopping");
                break;
            }

            List<User> users = tourGuideService.getAllUsers();
            logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
            stopWatch.start();
            users.forEach(u -> tourGuideService.trackUserLocation(u));
            stopWatch.stop();
            logger.debug("Tracker Time Elapsed: "
                    + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                    + " seconds.");
            stopWatch.reset();
            try {
                logger.debug("Tracker sleeping");
                TimeUnit.SECONDS.sleep(TRACKING_POLLING_INTERVAL);
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
