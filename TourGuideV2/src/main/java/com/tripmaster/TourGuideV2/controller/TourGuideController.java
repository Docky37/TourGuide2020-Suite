package com.tripmaster.TourGuideV2.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.dto.AttractionDTO;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.ProviderDTO;
import com.tripmaster.TourGuideV2.dto.UserPreferencesDTO;
import com.tripmaster.TourGuideV2.dto.UserRewardsDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;
import com.tripmaster.TourGuideV2.service.ITourGuideService;
import com.tripmaster.TourGuideV2.service.TourGuideService;

import io.swagger.annotations.ApiOperation;

/**
 * This controller exposes five endPoints to that gives TourGuide
 * functionalities.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
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
    private ITourGuideService tourGuideService;

    /**
     * HTTP GET request that returns a welcome message.
     *
     * @return a String, the welcome message
     */
    @ApiOperation(value = "Return a welcome message.", response = String.class)
    @GetMapping("/")
    public String index() {
        logger.info("New HTTP Request on /");
        return "Greetings from TourGuide!";
    }

    /**
     * HTTP GET request that returns the location of the user who responds to
     * the given userName parameter.
     *
     * @param userName
     * @return a String
     * @throws Throwable
     */
    @ApiOperation(value = "Return the GPS location of the user who responds"
            + " to the given userName parameter.",
            notes = "TourGuideV2 uses a WebClient to request an endpoint of"
                    + " GpsTools microservice that uses GpsUtil.jar.\n"
                    + "If you wanna test this endpoint, I think it will be"
                    + " helpfull for you to know the userName of all the 100"
                    + " users created for test, isn't it ?\n"
                    + "Don't worry, easy to remember that the first one is"
                    + " 'internalUser0' and the latest one 'internalUser99'!",
            response = LocationDTO.class)
    @GetMapping("/getLocation")
    public LocationDTO getLocation(@RequestParam final String userName)
            throws Throwable {
        logger.info("New HTTP Request on /getLocation for {}", userName);
        VisitedLocationDTO visitedLocationDTO = tourGuideService
                .getUserLocation(getUser(userName));
        logger.info(visitedLocationDTO.toString() + "\n");
        return visitedLocationDTO.getLocation();
    }

    /**
     * HTTP POST request that allows us to add a new visitedLocation.
     * Implemented for tests, to simulate incoming information of an attraction
     * visit to generate rewards.
     *
     * @param timeVisited
     * @param latitude
     * @param longitude
     * @param userName
     * @return a VisitedLocationDTO
     * @throws Throwable
     */
    @ApiOperation(value = "Allows you to add a new VisitedLocation for the user"
            + " who responds to the given userName parameter.\n",
            notes = "Implemented for tests, to simulate incoming information"
                    + " of an attraction visit in order to generate a reward."
                    + "If you wanna test this endpoint, I think it will be"
                    + " helpfull for you to know the userName of all the 100"
                    + " users created for test, isn't it ?\n"
                    + "Don't worry, easy to remember that the first one is"
                    + " 'internalUser0' and the latest one 'internalUser99'!",
            response = VisitedLocationDTO.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addVisitedLocation")
    public VisitedLocationDTO addVisitedLocation(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            final Date timeVisited,
            @RequestParam final double latitude,
            @RequestParam final double longitude,
            @RequestParam final String userName) throws Throwable {
        logger.info("New HTTP Post request on /addVisitedLocation for {}",
                userName);
        VisitedLocationDTO visitedLocationDTO = tourGuideService
                .addVisitedLocation(timeVisited, latitude, longitude,
                        getUser(userName));
        return visitedLocationDTO;
    }

    /**
     * HTTP GET request that return a list of all attractions.
     *
     * @return a List<AttractionDTO>
     */
    @ApiOperation(value = "Return the list of all attractions",
            notes = "The list of attractions is provided by GpsUtil.jar,"
            + " currently it only contains 26 attractions all located"
            + " in United States.", response = AttractionDTO.class,
            responseContainer = "List")
    @GetMapping("/getAllAttractions")
    public List<AttractionDTO> getAllAttractions() {
        List<Attraction> attractions = tourGuideService.getAllAttractions();
        List<AttractionDTO> attractionsDTO = new ArrayList<>();
        attractions.forEach(a -> {
            attractionsDTO.add(new AttractionDTO(a.getAttractionName(),
                    a.getCity(), a.getState(), a.getLatitude(),
                    a.getLongitude()));
        });
        return attractionsDTO;
    }

    /**
     * HTTP GET request that get the n closest tourist attractions to the user,
     * no matter how far away they are. The number n of attraction is defined by
     * the SIZE_OF_NEARBY_ATTRACTIONS_LIST constant of TourGuideService.
     * Currently set to 5.
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
     * @throws Throwable
     * @see AttractionsSuggestionDTO
     * @see NearbyAttractionDTO
     */
    @ApiOperation(value = "Returns the 5 closest tourist attractions to the"
            + "current user no matter how far away they are.",
            notes = "Returns an AttractionsSuggestionDTO that contains the user"
                    + " location, and a TreeMap with attractionName as String"
                    + " key and a NearbyAttractionDTO as value."
                    + " The attractionName is  prefixed by a index (1 to"
                    + " SIZE_OF_NEARBY_ATTRACTIONS_LIST constant (set to 5)"
                    + " in relation with the distance, in order to sort the"
                    + " TreeMap to display suggested attractions from the"
                    + " nearest to the farthest. The NearbyAttractionDTO"
                    + " contains the location (latitude, longitude) of the"
                    + " attraction, its distance from user location,"
                    + " and the reward points for its visit.\n"
                    + "If you wanna test this endpoint, I think it will be"
                    + " helpfull for you to know the userName of all the 100"
                    + " users created for test, isn't it ?\n"
                    + "Don't worry, easy to remember that the first one is"
                    + " 'internalUser0' and the latest one 'internalUser99'!",
                    response = AttractionsSuggestionDTO.class)
    @GetMapping("/getNearbyAttractions")
    public AttractionsSuggestionDTO getNearbyAttractions(
            @RequestParam final String userName) throws Throwable {
        logger.info("New HTTP Request on /getNearbyAttractions for {}",
                userName);
        AttractionsSuggestionDTO suggestion = tourGuideService
                .getAttractionsSuggestion(getUser(userName));
        logger.info(suggestion.toString() + "\n");
        return suggestion;
    }

    /**
     * HTTP GET request that returns a List of UserRewwards of the user who
     * responds to the given userName parameter.
     *
     * @param userName
     * @return a String the serialized List of UserRewwards
     * @throws Throwable
     */
    @ApiOperation(value = "Returns a list of UserRewards of the user who"
            + " responds to the given userName parameter.",
            notes = "TourGuideV2 uses a WebClient to request an endpoint of"
                    + "GetRewards microservice that uses GetPricer.jar.\n"
                    + "If you wanna test this endpoint, I think it will be"
                    + " helpfull for you to know the userName of all the 100"
                    + " users created for test, isn't it ?\n"
                    + "Don't worry, easy to remember that the first one is"
                    + " 'internalUser0' and the latest one 'internalUser99'!",
            response = UserRewardsDTO.class)
    @GetMapping("/getRewards")
    public UserRewardsDTO getRewards(@RequestParam final String userName)
            throws Throwable {
        logger.info("New HTTP Request on /getRewards for {}", userName);
        return tourGuideService.getUserRewards(getUser(userName));
    }

    /**
     * HTTP GET request that returns a List of every user's most recent location
     * using user's current location stored in location history.
     *
     * @return a JSON mapping of userId to Locations similar to:
     *         {"019b04a9-067a-4c76-8817-ee75088c3822":
     *         {"longitude":-48.188821,"latitude":74.84371} ... }
     */
    @ApiOperation(value = "Returns the list of all users' location.",
            notes = "The location of each user corresponds to the latest"
            + " location stored in his location history.")
    @GetMapping("/getAllCurrentLocations")
    public Map<String, LocationDTO> getAllCurrentLocations() {
        logger.info("New HTTP Request on /getAllCurrentLocations");
        return tourGuideService.getAllUsersLocation();
    }

    /**
     * HTTP GET request that finds user's preferences by his userName.
     *
     * @param userName
     * @return a UserPreferencesDTO
     * @throws Throwable
     */
    @ApiOperation(value = "Returns preferences of the users who"
            + " responds to the given userName parameter.",
            notes = "If you wanna test this endpoint, I think it will be"
                    + " helpfull for you to know the userName of all the 100"
                    + " users created for test, isn't it ?\n"
                    + "Don't worry, easy to remember that the first one is"
                    + " 'internalUser0' and the latest one 'internalUser99'!",
            response = UserPreferencesDTO.class)
    @GetMapping("/getUserPreferences")
    public UserPreferencesDTO getUserPreferences(
            @RequestParam final String userName) throws Throwable {
        UserPreferencesDTO userPref = tourGuideService
                .getPreferences(getUser(userName));

        return userPref;
    }

    /**
     * HTTP PUT request that allows user to modify his preferences.
     *
     * @param userName
     * @param userNewPreferencesDTO
     * @return a UserPreferencesDTO
     * @throws Throwable
     */
    @ApiOperation(value = "Allows us to update the preferences of the users who"
            + " responds to the given userName parameter.",
            notes = "TODO: This functionality need to be updated with a limited"
                    + " scope where user can only updates his own preferences."
                    + "\nIf you wanna test this endpoint, I think it will be"
                    + " helpfull for you to know the userName of all the 100"
                    + " users created for test, isn't it ?\n"
                    + "Don't worry, easy to remember that the first one is"
                    + " 'internalUser0' and the latest one 'internalUser99'!",
            response = UserPreferencesDTO.class)
    @PutMapping("/updatePreferences")
    public UserPreferencesDTO updatePreferences(
            @RequestParam final String userName,
            @RequestBody final UserPreferencesDTO userNewPreferencesDTO)
            throws Throwable {

        UserPreferencesDTO userPreferencesDTO = tourGuideService
                .updateUserPreferences(getUser(userName),
                        userNewPreferencesDTO);
        return userPreferencesDTO;
    }

    /**
     * HTTP GET request that returns a List of TripDeals suggested to the user
     * who responds to the given userName parameter.
     *
     * @param userName
     * @return a String
     * @throws Throwable
     */
    @ApiOperation(value = "Returns a List of TripDeals suggested to the user"
            + " who responds to the given userName parameter.",
            notes = "TourGuideV2 uses a WebClient to request an endpoint of"
                    + "TripDeals microservice that uses TripPricer.jar.\n"
                    + "Warning: It seems that TripPricer do not take in account"
                    + " all the users' preferences.\n"
                    + "If you wanna test this endpoint, I think it will be"
                    + " helpfull for you to know the userName of all the 100"
                    + " users created for test, isn't it ?\n"
                    + "Don't worry, easy to remember that the first one is"
                    + " 'internalUser0' and the latest one 'internalUser99'!",
            response = ProviderDTO.class, responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getTripDeals")
    public List<ProviderDTO> getTripDeals(@RequestParam final String userName)
            throws Throwable {
        logger.info("New HTTP Request on /getTripdeals for {}", userName);
        List<ProviderDTO> providers = tourGuideService
                .getTripDeals(getUser(userName));
        return providers;
    }

    /**
     * Returns the User who responds to the given userName parameter. Used by
     * previous HTTP request. This private sub method is used by previous HTTP
     * requests.
     *
     * @param userName
     * @return a User
     * @throws UserNotFoundException
     */
    private User getUser(final String userName) throws UserNotFoundException {
        User user = tourGuideService.getUser(userName);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException();
        }

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void addressFireStationNotFoundHandler(
            final UserNotFoundException e) {
        logger.info("END of Request with Status 404 NOT FOUND");
    }

}
