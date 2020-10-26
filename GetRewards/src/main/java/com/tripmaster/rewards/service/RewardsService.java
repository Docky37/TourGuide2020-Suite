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
     * Creation of GpsUtil instance.
     */
    @Autowired
    public RewardCentral rewardsCentral;

    /**
     * Class constructor.
     * 
     * @param rewardCentral
     */
    
    public RewardsService() {
        this.rewardsCentral = new RewardCentral();
    }
    
    public RewardsService(RewardCentral pRewardCentral) {
        this.rewardsCentral = pRewardCentral;
    }


    /**
     * This method calls a method of the RewardCentral.jar library in order to
     * get the attraction reward points for the given user.
     *
     * @param attraction
     * @param user
     * @return an int: the count of reward points
     */
    public int getRewardPoints(UUID attractionId, UUID userId) {
        logger.debug(" -> getRewardPoints(?,?", attractionId, userId);
        return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
