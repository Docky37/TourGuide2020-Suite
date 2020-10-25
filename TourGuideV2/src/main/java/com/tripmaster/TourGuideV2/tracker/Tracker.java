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
 * 
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Component
public class Tracker extends Thread {
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    private static final long trackingPollingInterval = TimeUnit.MINUTES
            .toSeconds(5);
    private final ExecutorService executorService = Executors
            .newSingleThreadExecutor();
    private final ITourGuideService tourGuideService;
    private boolean stop = false;

    /**
     * Class constructor.
     *
     * @param tourGuideService
     */
    public Tracker(ITourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
        executorService.submit(this);
    }

    /**
     * Assures to shut down the Tracker thread.
     */
    public void stopTracking() {
        stop = true;
        executorService.shutdownNow();
    }

    /**
     * Overridden Run super method.
     */
    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
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
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
