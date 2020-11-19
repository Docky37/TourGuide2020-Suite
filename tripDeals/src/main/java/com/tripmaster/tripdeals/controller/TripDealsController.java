package com.tripmaster.tripdeals.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripmaster.tripdeals.dto.ProviderDTO;
import com.tripmaster.tripdeals.service.ITripDealService;


/**
 * This TripDealsController class exposes one end-point used to get travel
 * proposals using the TripPricer application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@RestController
public class TripDealsController {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(TripDealsController.class);

    /**
     * TripDealService declaration. The bean is injected by Spring.
     */
    @Autowired
    private ITripDealService tripDealService;

    /**
     * HTML GET request on /getTripDeals used to get a list of ProviderDTO.
     *
     * @param tripPricerApiKey
     * @param userId
     * @param numberOfAdult
     * @param numberOfChildren
     * @param tripDuration
     * @param cumulatativeRewardPoints
     * @return a List<ProviderDTO>
     */
    @GetMapping("/getTripDeals")
    public List<ProviderDTO> getTripDeals(
            @RequestParam final String tripPricerApiKey,
            @RequestParam final UUID userId,
            @RequestParam final int numberOfAdults,
            @RequestParam final int numberOfChildren,
            @RequestParam final int tripDuration,
            @RequestParam final int cumulatativeRewardPoints) {
        logger.info("HTML GET Request /getTripDeals on localhost:8889");
        List<ProviderDTO> providerDTOList = tripDealService.getTripDeals(
                tripPricerApiKey, userId, numberOfAdults, numberOfChildren,
                tripDuration, cumulatativeRewardPoints);
        if (providerDTOList.isEmpty()) {
            logger.info("Sorry we don't find any trip deal!");
        }
        return providerDTOList;
    }

}
