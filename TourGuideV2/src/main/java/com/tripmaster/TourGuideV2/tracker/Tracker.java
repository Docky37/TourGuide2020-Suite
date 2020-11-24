package com.tripmaster.TourGuideV2.tracker;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.time.StopWatch;

import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.service.ITourGuideService;

/**
 * This class in charge of regularly launch the tracking of all users location.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */

public class Tracker  {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    /**
     * Defines the interval in minutes between two calls for users tracking.
     */
    private static final long TRACKING_POLLING_INTERVAL = TimeUnit.MINUTES
            .toSeconds(5);

    /**
     * Create an instance of a Fixed thread ExecutorService.
     */
    private final ExecutorService executorService = Executors
            .newFixedThreadPool(100);

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
        trackUsers();
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
    //@Override
    public void trackUsers() {
        StopWatch stopWatch = new StopWatch();
        while (true) {
            if (Thread.currentThread().isInterrupted() || isTrackerStopped) {
                logger.debug("Tracker stopping");
                break;
            }

            List<User> users = tourGuideService.getAllUsers();
            CountDownLatch countDownLatch = new CountDownLatch(users.size());
            logger.info("Begin Tracker. Tracking " + users.size() + " users.");
            stopWatch.start();

            users.forEach(u -> executorService.submit(() -> {
                tourGuideService.trackUserLocation(u);
                countDownLatch.countDown();
                System.out.println(countDownLatch.getCount());
            }));

            try {
                countDownLatch.await();
            } catch (InterruptedException e1) {
                logger.error(e1.getMessage());
            }

            stopWatch.stop();
            logger.info("Tracker Time Elapsed: "
                    + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
                    + " seconds.");
            stopWatch.reset();
            try {
                logger.info("Tracker sleeping");
                TimeUnit.SECONDS.sleep(TRACKING_POLLING_INTERVAL);
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
