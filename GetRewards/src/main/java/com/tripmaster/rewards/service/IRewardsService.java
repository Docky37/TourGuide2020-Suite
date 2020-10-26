package com.tripmaster.rewards.service;

import java.util.UUID;

public interface IRewardsService {

    int getRewardPoints(UUID attractionId, UUID userId);

}
