package com.tripmaster.TourGuideV2.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.UserReward;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.dto.AttractionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;
import com.tripmaster.TourGuideV2.helper.InternalTestHelper;
import com.tripmaster.TourGuideV2.tracker.Tracker;

import reactor.core.publisher.Flux;

/**
 * This service layer class is used dealing with external GpsUtil.jar and
 * RewardCentral.jar libraries to manage user rewards.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@Service
public class TourGuideService implements ITourGuideService {
    /**
     * Create a SLF4J/LOG4J LOGGER instance.
     */
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

    @Autowired
    public final Tracker tracker;

    /**
     * Create a testMode boolean instance initialized at true.
     */
    boolean testMode = true;

    /**
     * Class constructor
     *
     * @param gpsUtil
     * @param rewardsService
     */

    public TourGuideService() {
        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserReward> getUserRewards(User user) {
        return null; // user.getUserRewards();
    }

    /**
     * This method calls the VisitedLocations getter of User class and then if
     * the list is not empty, get the latest VisitedLocation , else it calls the
     * trackUserLocation method.
     *
     * @param user
     * @return a VisitedLocation
     */
    @Override
    public VisitedLocationDTO getUserLocation(User user) {
        VisitedLocation visitedLocation;
        VisitedLocationDTO visitedLocationDTO;
        if (user.getVisitedLocations().size() > 0) {
            visitedLocation = user.getLastVisitedLocation();
            visitedLocationDTO = new VisitedLocationDTO(
                    new LocationDTO(visitedLocation.getLocation().getLatitude(),
                            visitedLocation.getLocation().getLongitude()),
                    visitedLocation.getTimeVisited());

        } else {
            visitedLocationDTO = trackUserLocation(user);
            visitedLocation = new VisitedLocation(
                    new Location(visitedLocationDTO.getLocation().getLatitude(),
                            visitedLocationDTO.getLocation().getLongitude()),
                    visitedLocationDTO.getTimeVisited());
            user.addToVisitedLocations(visitedLocation);
            // rewardsService.calculateRewards(user);
        }

        return visitedLocationDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAllUsers() {
        return internalUserMap.values().stream().collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    /**
     * {@inheritDoc}
     */
    /*
     * @Override public List<Provider> getTripDeals(User user) { int
     * cumulatativeRewardPoints = user.getUserRewards().stream() .mapToInt(i ->
     * i.getRewardPoints()).sum(); List<Provider> providers =
     * tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
     * user.getUserPreferences().getNumberOfAdults(),
     * user.getUserPreferences().getNumberOfChildren(),
     * user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
     * user.setTripDeals(providers); return providers; }
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public VisitedLocationDTO trackUserLocation(User user) {
        final String uri = "http://localhost:8889//getLocation?userId="
                + user.getUserId();
        RestTemplate restTemplate = new RestTemplate();
        VisitedLocationDTO visitedLocationDTO = restTemplate.getForObject(uri,
                VisitedLocationDTO.class);
        return visitedLocationDTO;
    }

    /**
     * Get the list of the n closest attractions. The number n is defined by the
     * SIZE_OF_NEARBY_ATTRACTIONS_LIST constant.
     *
     * @param visitedLocation
     * @return a List<Attraction>
     */
    @Override
    public List<Attraction> getNearByAttractions(
            VisitedLocation visitedLocation) {
        final String attractionUri = "/getAllAttractions";
        WebClient webClient = WebClient.create("http://localhost:8889");

        Flux<AttractionDTO> attractionsFlux = webClient.get()
                .uri(attractionUri)
                .retrieve()
                .bodyToFlux(AttractionDTO.class);
        List<AttractionDTO> listOfAttractionDTO = (List<AttractionDTO>) attractionsFlux
                .collectList();

        List<Attraction> nearbyFiveAttractions = new ArrayList<>();

        listOfAttractionDTO.forEach(
                a -> {
                    nearbyFiveAttractions.add(
                            new Attraction(a.getAttractionName(), a.getCity(),
                                    a.getState(), a.getLatitude(),
                                    a.getLongitude()));
                }

        );

        /*
         * nearbyFiveAttractions = attractions.stream()
         * .sorted(Comparator.comparingDouble(a -> rewardsService
         * .getDistance(visitedLocation.location, a)))
         * .limit(SIZE_OF_NEARBY_ATTRACTIONS_LIST)
         * .collect(Collectors.toList()); nearbyFiveAttractions.forEach( a ->
         * logger.debug("getNearByAttractions:" + a.attractionName));
         */
        return nearbyFiveAttractions;
    }

    /**
     * {@inheritDoc}
     *
     * @param user
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
     * @see getNearByAttractions(VisitedLocation visitedLocation)
     */
    /*
     * @Override public AttractionsSuggestionDTO getAttractionsSuggestion(User
     * user) { AttractionsSuggestionDTO suggestion = new
     * AttractionsSuggestionDTO();
     * suggestion.setUserLocation(user.getLastVisitedLocation().location);
     * TreeMap<String, NearbyAttractionDTO> suggestedAttractions = new
     * TreeMap<>(); List<Attraction> attractionsList = getNearByAttractions(
     * user.getLastVisitedLocation()); final AtomicInteger indexHolder = new
     * AtomicInteger(1); attractionsList.stream()
     * .sorted(Comparator.comparingDouble(a -> rewardsService
     * .getDistance(user.getLastVisitedLocation().location, a))) .forEach(a -> {
     * final int index = indexHolder.getAndIncrement();
     * suggestedAttractions.put( index + ". " + a.attractionName, new
     * NearbyAttractionDTO( new Location(a.latitude, a.longitude),
     * rewardsService.getDistance(a, user.getLastVisitedLocation().location),
     * rewardsService.getRewardPoints(a, user))); });
     * 
     * suggestion.setSuggestedAttractions(suggestedAttractions);
     * 
     * return suggestion; }
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, LocationDTO> getAllUsersLocation() {
        Map<String, LocationDTO> locationsDTO = new HashMap<>();
        getAllUsers().forEach(u -> {
            locationsDTO.put(u.getUserId().toString(),
                    new LocationDTO(
                            getUserLocation(u).getLocation().getLatitude(),
                            getUserLocation(u).getLocation().getLongitude()));
        });

        return locationsDTO;
    }

    /**
     * Used to shut down the tracker thread.
     */
    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }

    /**********************************************************************************
     * 
     * Methods Below: For Internal Testing
     * 
     **********************************************************************************/
    private static final String tripPricerApiKey = "test-server-api-key";
    // Database connection will be used for external users, but for testing
    // purposes internal users are provided and stored in memory
    private final Map<String, User> internalUserMap = new HashMap<>();

    /**
     * This method creates users for tests.
     */
    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber())
                .forEach(i -> {
                    String userName = "internalUser" + i;
                    String phone = "000";
                    String email = userName + "@tourGuide.com";
                    User user = new User(UUID.randomUUID(), userName, phone,
                            email);
                    generateUserLocationHistory(user);
                    logger.debug("user = " + user.toString());
                    internalUserMap.put(userName, user);
                });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber()
                + " internal test users.");
    }

    /**
     * This method creates an history of 3 visited location for the given user,
     * calling 3 sub method to generate randomized latitude, longitude and time.
     *
     * @param user
     */
    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(
                    new VisitedLocation(user.getUserId(),
                            new Location(generateRandomLatitude(),
                                    generateRandomLongitude()),
                            getRandomTime()));
        });
    }

    /**
     * This method return a randomized valid longitude.
     *
     * @return a double
     */
    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * This method return a randomized valid latitude.
     *
     * @return a double
     */
    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * This method return a randomized LocalDateTime .
     *
     * @return a LocalDateTime
     */
    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now()
                .minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

}
