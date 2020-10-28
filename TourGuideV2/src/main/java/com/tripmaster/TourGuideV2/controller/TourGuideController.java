package com.tripmaster.TourGuideV2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;
import com.tripmaster.TourGuideV2.service.ITourGuideService;
import com.tripmaster.TourGuideV2.service.TourGuideService;

@RestController
public class TourGuideController {

    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

    /**
     * Asks Spring to inject a TourGuideService bean when TourGuideController is
     * created.
     */
    @Autowired
    ITourGuideService tourGuideService;

    /**
     * HTML GET request that returns a welcome message.
     *
     * @return a String, the welcome message
     */
    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * HTML GET request that returns the location of the user who responds to
     * the given userName parameter.
     *
     * @param userName
     * @return a String
     */
    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        logger.info("New HTML Request on /getLocation for ?", userName);
        VisitedLocationDTO visitedLocationDTO = tourGuideService
                .getUserLocation(getUser(userName));
        logger.info(visitedLocationDTO.toString() + "\n");
        return JsonStream.serialize(visitedLocationDTO.getLocation());
    }

    /**
     * HTML GET request that get the n closest tourist attractions to the user,
     * no matter how far away they are. The number n of attraction is defined by
     * the SIZE_OF_NEARBY_ATTRACTIONS_LIST constant of TourGuideService.
     *
     * @param userName
     * @return an AttractionsSuggestionDTO that contains the user location, and
     *         a TreeMap with attractionName as String key and a
     *         NearbyAttractionDTO as value. The attractionName is prefixed by a
     *         index (1 to SIZE_OF_NEARBY_ATTRACTIONS_LIST constant) which was
     *         set in relation with the distance, in order to sort the TreeMap
     *         to display suggested attractions from the nearest to the farthest
     *         The NearbyAttractionDTO contains the location (latitude,
     *         longitude) of the attraction, its distance from user location,
     *         and the reward points for its visit.
     * @see AttractionsSuggestionDTO
     * @see NearbyAttractionDTO
     */
    @GetMapping("/getNearbyAttractions")
    public AttractionsSuggestionDTO getNearbyAttractions(
            @RequestParam String userName) {
        logger.info("New HTML Request on /getNearbyAttractions for ?",
                userName);
        AttractionsSuggestionDTO suggestion = tourGuideService.getAttractionsSuggestion(getUser(userName));
        logger.info(suggestion.toString() + "\n");
        return suggestion;
    }

    /**
     * HTML GET request that returns a List of UserRewwards of the user who
     * responds to the given userName parameter.
     *
     * @param userName
     * @return a String the serialized List of UserRewwards
     */
    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        logger.info("New HTML Request on /getRewards for ?", userName);
        return JsonStream
                .serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    /**
     * HTML GET request that returns a List of every user's most recent location
     * using user's current location stored in location history.
     *
     * @return a JSON mapping of userId to Locations similar to:
     *         {"019b04a9-067a-4c76-8817-ee75088c3822":
     *         {"longitude":-48.188821,"latitude":74.84371} ... }
     */
    @GetMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(tourGuideService.getAllUsersLocation());
    }

    /**
     * HTML GET request that returns a List of TripDeals suggested to the user
     * who responds to the given userName parameter.
     *
     * @param userName
     * @return a String
     */
    /*
     * @GetMapping("/getTripDeals") public String getTripDeals(@RequestParam
     * String userName) { List<Provider> providers = tourGuideService
     * .getTripDeals(getUser(userName)); return JsonStream.serialize(providers);
     * }
     */

    /**
     * Returns the User who responds to the given userName parameter. used by
     * previous HTML request. This private sub method is used by pevious html
     * requests.
     *
     * @param userName
     * @return
     */
    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }

}
