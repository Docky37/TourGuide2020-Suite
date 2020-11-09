package com.tripmaster.rewards.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rewardCentral.RewardCentral;

/**
 * This service layer class is used to deal with the external RewardCentral.jar
 * library to manage user rewards.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Service
public class RewardsService implements IRewardsService {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(RewardsService.class);

    /**
     * Declares a GpsUtil instance, that will be initialized by Spring
     * injection.
     */
    @Autowired
    private RewardCentral rewardsCentral;

    /**
     * No parameter class constructor.
     */
    public RewardsService() {
    }

    /**
     * Class constructor with one parameter.
     *
     * @param pRewardCentral
     */
    public RewardsService(final RewardCentral pRewardCentral) {
        this.rewardsCentral = pRewardCentral;
    }

    /**
     * This method calls a method of the RewardCentral.jar library in order to
     * get the attraction reward points for the given user.
     *
     * @param attractionId
     * @param userId
     * @return an int: the count of reward points
     */
    public int getRewardPoints(final UUID attractionId, final UUID userId) {
        logger.debug(" -> getRewardPoints(?,?", attractionId, userId);
        return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
