package com.tripmaster.rewards.service;

import java.util.UUID;

/**
 * This interface de fine one method to get reward points from RewardCentral
 * application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public interface IRewardsService {

    /**
     * This method is in charge of requesting RewardSCentral.jar to get the
     * reward points obtained by the given user who has vistited the given
     * attraction.
     *
     * @param attractionId
     * @param userId
     * @return an int - the number of points obtained
     */
    int getRewardPoints(UUID attractionId, UUID userId);

}
