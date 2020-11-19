package com.tripmaster.rewards.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripmaster.rewards.service.IRewardsService;

/**
 * The controller of GetRewards application exposes one endpoint (/getReward)
 * that allows us to get the reward points obtained following an attraction
 * visit.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@RestController
public class RewardsController {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(RewardsController.class);

    /**
     * RewardsService declaration, for Spring injection.
     */
    @Autowired
    private IRewardsService rewardsService;

    /**
     * HTML GET request used to get reward points from RewardsCentral jar
     * library.
     *
     * @param attractionId
     * @param userId
     * @return an int
     */
    @GetMapping("/getReward")
    public int getUserRewards(@RequestParam final UUID attractionId,
            @RequestParam final UUID userId) {
        logger.info("HTML GET Request on localhost:8787");
        int rewardPoints = rewardsService.getRewardPoints(attractionId, userId);
        logger.info(" User {} reward = {} points", userId, rewardPoints);
        return rewardPoints;
    }

}
