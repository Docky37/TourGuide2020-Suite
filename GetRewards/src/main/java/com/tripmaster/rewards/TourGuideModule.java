package com.tripmaster.rewards;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tripmaster.rewards.service.IRewardsService;
import com.tripmaster.rewards.service.RewardsService;

import rewardCentral.RewardCentral;

@Configuration
public class TourGuideModule {
	
	@Bean
	public IRewardsService getRewardsService() {
		return new RewardsService(getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
