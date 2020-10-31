package com.tripmaster.tripdeals.service;

import java.util.List;
import java.util.UUID;

import com.tripmaster.tripdeals.dto.ProviderDTO;

public interface ITripDealService {

    List<ProviderDTO> getTripDeals(String tripPricerApiKey, UUID userId,
            int NumberOfAdult, int numberOfChildren, int tripDuration,
            int cumulatativeRewardPoints);

}
