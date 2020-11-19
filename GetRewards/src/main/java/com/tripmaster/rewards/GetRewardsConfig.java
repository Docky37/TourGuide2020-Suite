package com.tripmaster.rewards;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * RewardCentral bean for Spring injection.
     *
     * @return a RewardCentral instance
     */
    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }

}
