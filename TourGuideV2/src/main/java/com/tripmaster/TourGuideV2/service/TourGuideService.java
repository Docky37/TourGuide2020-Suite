package com.tripmaster.TourGuideV2.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.UserReward;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.NearbyAttractionDTO;
import com.tripmaster.TourGuideV2.dto.ProviderDTO;
import com.tripmaster.TourGuideV2.dto.UserRewardDTO;
import com.tripmaster.TourGuideV2.dto.UserRewardsDTO;
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

    /**
     * RewardService declaration, the bean is injected by Spring with the class
     * constructor @Autowired annotation.
     */
    IRewardsService rewardsService;

    /**
     * A TripDeals Webclient declaration. The bean is injected by Spring with
     * the class constructor @Autowired annotation.
     */
    WebClient webClientTripDeals;

    /**
     * A GpsTools Webclient declaration. The bean is injected by Spring with the
     * class constructor @Autowired annotation.
     */
    WebClient webClientGps;

    /**
     * 
     */
    public final Tracker tracker;

    /**
     * Create a testMode boolean instance initialized at true.
     */
    boolean testMode = true;

    /**
     * This class constructor allows Spring to inject 3 beans, RewardsService
     * and 2 WebClient beans discriminated against by the @Qualifier annotation.
     *
     * @param pRewardsService
     * @param pWebClientTripDeals
     * @param pWebClientGps
     */
    @Autowired
    public TourGuideService(final IRewardsService pRewardsService,
            @Qualifier("getWebClientTripDeals") final WebClient pWebClientTripDeals,
            @Qualifier("getWebClientGps") final WebClient pWebClientGps)
    {
        rewardsService = pRewardsService;
        webClientGps = pWebClientGps;
        webClientTripDeals = pWebClientTripDeals;
        classInitialization();
        tracker = new Tracker(this);
        addShutDownHook();
    }

    /**
     * This method is used in test mode to initialize service calling a method
     * that creates random data.
     */
    private void classInitialization() {
        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
    }

    /**
     * Specific class constructor for unit tests, thats allows Spring to inject
     * mocks.
     *
     * @param test
     * @param pRewardsService
     * @param pWebClientTripDeals
     * @param pWebClientGps
     */
    public TourGuideService(final String test,
            final IRewardsService pRewardsService,
            final WebClient pWebClientTripDeals, final WebClient pWebClientGps)
    {
        rewardsService = pRewardsService;
        rewardsService = pRewardsService;
        webClientTripDeals = pWebClientTripDeals;
        webClientGps = pWebClientGps;

        classInitialization();
        tracker = new Tracker(this);
        addShutDownHook();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserRewardsDTO getUserRewards(User user) {
        List<UserReward> userRewards = user.getUserRewards();
        UserRewardsDTO userRewardsDTO = new UserRewardsDTO();
        userRewardsDTO.setUserName(user.getUserName());
        userRewards.forEach(uR -> userRewardsDTO
                .addUserRewardDTO(new UserRewardDTO(
                        uR.visitedLocation,
                        uR.attraction,
                        uR.getRewardPoints())));
        return userRewardsDTO;
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
                    visitedLocation.getTimeVisited(),
                    visitedLocation.getUserId());

        } else {
            visitedLocationDTO = trackUserLocation(user);
            visitedLocation = new VisitedLocation(
                    new Location(visitedLocationDTO.getLocation().getLatitude(),
                            visitedLocationDTO.getLocation().getLongitude()),
                    visitedLocationDTO.getTimeVisited());
            user.addToVisitedLocations(visitedLocation);
            rewardsService.calculateRewards(user, getAllAttractions());
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
    // @Override
    public List<ProviderDTO> getTripDeals(User user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream()
                .mapToInt(i -> i.getRewardPoints()).sum();

        Flux<ProviderDTO> flux = webClientTripDeals.get()
                .uri("/getTripDeals?tripPricerApiKey=" + tripPricerApiKey
                        + "&userId=" + user.getUserId()
                        + "&numberOfAdults=" + user.getUserPreferences()
                                .getNumberOfAdults()
                        + "&numberOfChildren=" + user.getUserPreferences()
                                .getNumberOfChildren()
                        + "&tripDuration=" + user.getUserPreferences()
                                .getTripDuration()
                        + "&cumulatativeRewardPoints="
                        + cumulatativeRewardPoints)
                .retrieve()
                .bodyToFlux(ProviderDTO.class);
        System.out.println(flux.toString());
        List<ProviderDTO> providers = flux.collectList().block();

        user.setTripDeals(providers);
        return providers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisitedLocationDTO trackUserLocation(User user) {
        final String getLocationUri = "/getLocation?userId=\"\r\n" 
                + user.getUserId();

        VisitedLocationDTO visitedLocationDTO = webClientGps.get()
                .uri(getLocationUri)
                .retrieve()
                .bodyToMono(VisitedLocationDTO.class).block();

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
        List<Attraction> listOfAttraction = getAllAttractions();

        List<Attraction> nearbyFiveAttractions = new ArrayList<>();

        nearbyFiveAttractions = listOfAttraction.stream()
                .sorted(Comparator.comparingDouble(a -> rewardsService
                        .getDistance(a, visitedLocation.getLocation())))
                .limit(SIZE_OF_NEARBY_ATTRACTIONS_LIST)
                .collect(Collectors.toList());
        nearbyFiveAttractions.forEach(a -> logger
                .debug("getNearByAttractions:" + a.getAttractionName()));

        return nearbyFiveAttractions;
    }

    @Override
    public List<Attraction> getAllAttractions() {
        final String attractionUri = "/getAllAttractions";

        Flux<Attraction> attractionsFlux = webClientGps.get()
                .uri(attractionUri)
                .retrieve()
                .bodyToFlux(Attraction.class);

        List<Attraction> listOfAttraction = attractionsFlux.collectList()
                .block();

        return listOfAttraction;
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
    @Override
    public AttractionsSuggestionDTO getAttractionsSuggestion(User user) {

        AttractionsSuggestionDTO suggestion = new AttractionsSuggestionDTO();
        suggestion.setUserLocation(new LocationDTO(
                user.getLastVisitedLocation().getLocation().getLatitude(),
                user.getLastVisitedLocation().getLocation().getLatitude()));

        TreeMap<String, NearbyAttractionDTO> suggestedAttractions = new TreeMap<>();
        List<Attraction> attractionsList = getNearByAttractions(
                user.getLastVisitedLocation());
        final AtomicInteger indexHolder = new AtomicInteger(1);
        attractionsList.stream()
                .sorted(Comparator.comparingDouble(a -> rewardsService
                        .getDistance(a,
                                user.getLastVisitedLocation().getLocation())))
                .forEach(a -> {
                    final int index = indexHolder.getAndIncrement();
                    suggestedAttractions.put(
                            index + ". " + a.getAttractionName(),
                            new NearbyAttractionDTO(
                                    new LocationDTO(a.getLatitude(),
                                            a.getLongitude()),
                                    rewardsService.getDistance(a,
                                            user.getLastVisitedLocation()
                                                    .getLocation()),
                                    rewardsService.getRewardPoints(a, user)));
                });

        suggestion.setSuggestedAttractions(suggestedAttractions);

        return suggestion;
    }

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
        List<Attraction> attractions = getAllAttractions();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        IntStream.range(0, InternalTestHelper.getInternalUserNumber())
                .forEach(i -> executor.submit(() -> {
                    String userName = "internalUser" + i;
                    String phone = "000";
                    String email = userName + "@tourGuide.com";
                    User user = new User(UUID.randomUUID(), userName, phone,
                            email);
                    generateUserLocationHistory(user, attractions);
                    logger.debug("user = " + user.toString());
                    internalUserMap.put(userName, user);
                }));
        executor.shutdown();
        try {
            if (!executor.awaitTermination(2000,
                    TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        logger.debug("Created " + InternalTestHelper.getInternalUserNumber()
                + " internal test users.");
    }

    /**
     * This method creates an history of 3 visited location for the given user,
     * calling 3 sub method to generate randomized latitude, longitude and time.
     *
     * @param user
     */
    private void generateUserLocationHistory(User user,
            List<Attraction> attractions) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(
                    new VisitedLocation(user.getUserId(),
                            new Location(generateRandomLatitude(),
                                    generateRandomLongitude()),
                            getRandomTime()));
        });
        //rewardsService.calculateRewards(user, attractions);

    }

    /**
     * This method return a randomized valid longitude.
     *
     * @return a double
     */
    private double generateRandomLongitude() {
        double leftLimit = -125;// -180;
        double rightLimit = -66;// 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * This method return a randomized valid latitude.
     *
     * @return a double
     */
    private double generateRandomLatitude() {
        double leftLimit = 28.0;// -85.05112878;
        double rightLimit = 42.0;// 85.05112878;
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
