package com.tripmaster.rewards;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tripmaster.rewards.service.IRewardsService;
import com.tripmaster.rewards.service.RewardsService;

import rewardCentral.RewardCentral;

/**
 * This class defines 2 beans candidates for Spring injection in GetRewards
 * application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Configuration
public class GetRewardsConfig {

    /**
     * IRewardsService bean for Spring injection.
     *
     * @return a RewardsService
     */
    @Bean
    public IRewardsService getRewardsService() {
        return new RewardsService(getRewardCentral());
    }

    /**
     * RewardCentral bean for Spring injection.
     *
     * @return a RewardCentral instance
     */
    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }

}
