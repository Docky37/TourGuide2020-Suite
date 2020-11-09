package com.tripmaster.tripdeals.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tripmaster.tripdeals.dto.ProviderDTO;

import tripPricer.Provider;
import tripPricer.TripPricer;

/**
 * This class contains one method used to get TripDeals propositions from
 * TripPricer application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Service
public class TripDealService implements ITripDealService {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(TripDealService.class);

    /**
     * Declaration and initialization of a TripPricer instance.
     */
    private TripPricer tripPricer = new TripPricer();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProviderDTO> getTripDeals(final String tripPricerApiKey,
            final UUID userId, final int numberOfAdult,
            final int numberOfChildren, final int tripDuration,
            final int cumulatativeRewardPoints) {
        logger.debug(" -> getTripDeals");

        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, userId,
                numberOfAdult, numberOfChildren, tripDuration,
                cumulatativeRewardPoints);

        List<ProviderDTO> providerDTOList = new ArrayList<>();
        providers.forEach(p -> {
            providerDTOList.add(new ProviderDTO(
                    p.tripId, p.name, p.price));
        });
        return providerDTOList;
    }

}
