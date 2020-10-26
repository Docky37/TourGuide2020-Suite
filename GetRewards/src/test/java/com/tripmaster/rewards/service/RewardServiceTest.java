package com.tripmaster.rewards.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import rewardCentral.RewardCentral;

@ExtendWith(SpringExtension.class)
public class RewardServiceTest {
    
    @Mock
    RewardCentral rewardsCentral;
    
    @InjectMocks
    IRewardsService rewardService = new RewardsService(rewardsCentral);
    
    
    @Test
    public void givenAnAttractionIdAndAUserId_whenGetRewardPoints_thenReturnAnInt() {
        // GIVEN
        UUID attractionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        given(rewardsCentral.getAttractionRewardPoints(any(UUID.class), any(UUID.class))).willReturn(77);
        // WHEN
        int rewardPoints = rewardService.getRewardPoints(attractionId, userId);
        // THEN
        
        assertThat(rewardPoints).isEqualTo(77);
    }

}
