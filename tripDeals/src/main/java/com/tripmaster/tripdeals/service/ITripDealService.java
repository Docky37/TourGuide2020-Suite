package com.tripmaster.tripdeals.service;

import java.util.List;
import java.util.UUID;

import com.tripmaster.tripdeals.dto.ProviderDTO;

/**
 * This interface defines one method used to get TripDeals propositions from
 * TripPricer application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */

public interface ITripDealService {

    /**
     * This method is in charge of requesting TripPricer application in order to
     * obtain TripDeals propositions in relation with user's reward points and
     * preferences.
     *
     * @param tripPricerApiKey
     * @param userId
     * @param numberOfAdult
     * @param numberOfChildren
     * @param tripDuration
     * @param cumulatativeRewardPoints
     * @return a List<ProviderDTO>
     */
    List<ProviderDTO> getTripDeals(String tripPricerApiKey, UUID userId,
            int numberOfAdult, int numberOfChildren, int tripDuration,
            int cumulatativeRewardPoints);

}
