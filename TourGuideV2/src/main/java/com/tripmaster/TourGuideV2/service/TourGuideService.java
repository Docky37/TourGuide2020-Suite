package com.tripmaster.TourGuideV2.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.UserPreferences;
import com.tripmaster.TourGuideV2.domain.UserReward;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.domain.Location;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.NearbyAttractionDTO;
import com.tripmaster.TourGuideV2.dto.ProviderDTO;
import com.tripmaster.TourGuideV2.dto.UserPreferencesDTO;
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
    private IRewardsService rewardsService;

    /**
     * A TripDeals Webclient declaration. The bean is injected by Spring with
     * the class constructor @Autowired annotation.
     */
    private WebClient webClientTripDeals;

    /**
     * A GpsTools Webclient declaration. The bean is injected by Spring with the
     * class constructor @Autowired annotation.
     */
    private WebClient webClientGps;

    /**
     * A Tracker declaration. The tracker instance is create later in the class
     * constructor.
     */
    private final Tracker tracker;

    /**
     * Create a testMode boolean instance initialized at true.
     */
    private boolean testMode = true;

    /**
     * Create a locale list of attractions to avoid many calls on GpsUtil
     * because this list has a low update frequency. This list is created
     * when service is launched and need a daily update.
     */
    private List<Attraction> attractions;

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
            @Qualifier("getWebClientGps") final WebClient pWebClientGps) {
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
    public void classInitialization() {
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
            final WebClient pWebClientTripDeals,
            final WebClient pWebClientGps) {
        rewardsService = pRewardsService;
        rewardsService = pRewardsService;
        webClientTripDeals = pWebClientTripDeals;
        webClientGps = pWebClientGps;

        classInitialization();
        tracker = null; // We don't want to use tracker during unit tests!
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserRewardsDTO getUserRewards(final User user) {
        List<UserReward> userRewards = user.getUserRewards();
        UserRewardsDTO userRewardsDTO = new UserRewardsDTO();
        userRewardsDTO.setUserName(user.getUserName());
        userRewards.forEach(uR -> userRewardsDTO
                .addUserRewardDTO(new UserRewardDTO(
                        uR.getVisitedLocation(),
                        uR.getAttraction(),
                        uR.getRewardPoints())));
        return userRewardsDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisitedLocationDTO trackUserLocation(final User user) {
        final String getLocationUri = "/getUserLocation?userId="
                + user.getUserId();

        Optional<VisitedLocationDTO> optVisitedLocationDTO = Optional
                .ofNullable(webClientGps.get()
                        .uri(getLocationUri)
                        .retrieve()
                        .bodyToMono(VisitedLocationDTO.class)
                        .block());

        VisitedLocationDTO visitedLocationDTO = optVisitedLocationDTO
                .orElseThrow();
        saveNewVisitedLocation(user, visitedLocationDTO);
        return visitedLocationDTO;
    }

    /**
     * Private method use to map the new VisitedLocationDTO to VisitedLocation
     * and add it to user.visitedLocations.
     *
     * @param user
     * @param visitedLocationDTO
     */
    private void saveNewVisitedLocation(final User user,
            final VisitedLocationDTO visitedLocationDTO) {
        user.addToVisitedLocations(new VisitedLocation(
                new Location(visitedLocationDTO.getLocation().getLatitude(),
                        visitedLocationDTO.getLocation().getLongitude()),
                visitedLocationDTO.getTimeVisited()));
        rewardsService.calculateRewards(user, getAttractions());

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
    public VisitedLocationDTO getUserLocation(final User user) {
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
        }

        return visitedLocationDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisitedLocationDTO addVisitedLocation(final Date timeVisited,
            final double latitude, final double longitude, final User user) {
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),
                new Location(latitude, longitude), timeVisited);

        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user, getAttractions());

        return getUserLocation(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(final String userName) {
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
    public void addUser(final User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        } else {
            logger.debug(
                    "This userName '{}' already exists in internalUserMap!",
                    user.getUserName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPreferencesDTO getPreferences(final User user) {
        UserPreferences userPreferences = user.getUserPreferences();
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        userPreferencesDTO.setAttractionProximity(
                userPreferences.getAttractionProximity());
        userPreferencesDTO.setHighPricePoint(
                userPreferences.getHighPricePoint().getNumber().intValue());
        userPreferencesDTO.setLowerPricePoint(
                userPreferences.getLowerPricePoint().getNumber().intValue());
        userPreferencesDTO.setNumberOfAdults(
                userPreferences.getNumberOfAdults());
        userPreferencesDTO.setNumberOfChildren(
                userPreferences.getNumberOfChildren());
        userPreferencesDTO.setTicketQuantity(
                userPreferences.getTicketQuantity());
        userPreferencesDTO.setTripDuration(
                userPreferences.getTripDuration());

        return userPreferencesDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPreferencesDTO updateUserPreferences(final User user,
            final UserPreferencesDTO userNewPreferencesDTO) {
        UserPreferences userPreferences = user.getUserPreferences();
        userPreferences.setAttractionProximity(
                userNewPreferencesDTO.getAttractionProximity());
        userPreferences.setHighPricePoint(Money.of(
                userNewPreferencesDTO.getHighPricePoint(),
                userPreferences.getCurrency()));
        userPreferences.setLowerPricePoint(Money.of(
                userNewPreferencesDTO.getLowerPricePoint(),
                userPreferences.getCurrency()));
        userPreferences.setNumberOfAdults(
                userNewPreferencesDTO.getNumberOfAdults());
        userPreferences.setNumberOfChildren(
                userNewPreferencesDTO.getNumberOfChildren());
        userPreferences.setTicketQuantity(
                userNewPreferencesDTO.getTicketQuantity());
        userPreferences.setTripDuration(
                userNewPreferencesDTO.getTripDuration());

        return getPreferences(user);
    }

    /**
     * {@inheritDoc}
     */
    // @Override
    public List<ProviderDTO> getTripDeals(final User user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream()
                .mapToInt(i -> i.getRewardPoints()).sum();

        try {
            Flux<ProviderDTO> flux = webClientTripDeals.get()
                    .uri("/getTripDeals?tripPricerApiKey=" + TRIP_PRICER_API_KEY
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

            List<ProviderDTO> providers = flux.collectList().block();
            user.setTripDeals(providers);
            return providers;
        } catch (WebClientResponseException e) {
            logger.error(e.toString());
        }
        return new ArrayList<ProviderDTO>();
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
            final VisitedLocation visitedLocation) {
        List<Attraction> listOfAttraction = getAttractions();

        List<Attraction> nearbyFiveAttractions = listOfAttraction.stream()
                .sorted(Comparator.comparingDouble(a -> rewardsService
                        .getDistance(a, visitedLocation.getLocation())))
                .limit(SIZE_OF_NEARBY_ATTRACTIONS_LIST)
                .collect(Collectors.toList());
        nearbyFiveAttractions.forEach(a -> logger
                .debug("getNearByAttractions:" + a.getAttractionName()));

        return nearbyFiveAttractions;
    }

    /**
     * {@inheritDoc}
     */
    public List<Attraction> getAllAttractionsFromGpsTools() {
        final String attractionUri = "/getAllAttractions";

        Flux<Attraction> attractionsFlux = webClientGps.get()
                .uri(attractionUri)
                .retrieve()
                .bodyToFlux(Attraction.class);

        List<Attraction> listOfAttraction = attractionsFlux.collectList()
                .block();
        setAttractions(listOfAttraction);
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
    public AttractionsSuggestionDTO getAttractionsSuggestion(final User user) {

        AttractionsSuggestionDTO suggestion = new AttractionsSuggestionDTO();
        suggestion.setUserLocation(new LocationDTO(
                user.getLastVisitedLocation().getLocation().getLatitude(),
                user.getLastVisitedLocation().getLocation().getLongitude()));

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

    /*
     * *************************************************************************
     *
     * Methods Below: For Internal Testing.
     *
     * *************************************************************************
     */

    /**
     * The API Key that is mandatory to use TripPricer.
     */
    public static final String TRIP_PRICER_API_KEY = "test-server-api-key";

    /**
     * Database connection will be used for external users, but for testing
     * purposes internal users are provided and stored in memory.
     */
    private final Map<String, User> internalUserMap = new HashMap<>();

    /**
     * Defines the number of visitedLocations to create for each user.
     */
    private static final int NUMBER_OF_USER_VISITED_LOCATIONS_TO_CREATE = 3;

    /**
     * Define the north limit of user's locations area (USA for Tests).
     */
    private static final double LATITUDE_NORTH_LIMIT = 42;
    /**
     * Define the south limit of user's locations area (USA for Tests).
     */
    private static final double LATITUDE_SOUTH_LIMIT = 25;
    /**
     * Define the west limit of user's locations area (USA for Tests).
     */
    private static final double LONGITUDE_WEST_LIMIT = -125;
    /**
     * Define the east limit of user's locations area (USA for Tests).
     */
    private static final double LONGITUDE_EAST_LIMIT = -66;

    /**
     * Number 30 used in time randomization.
     */
    private static final int THIRTY = 30;

    /**
     * Constant that we reuse inside the constructor with.
     */
    private static final Random RANDOM = new Random();

    /**
     * This method creates users for tests.
     */
    private void initializeInternalUsers() {
        List<Attraction> attractions = getAllAttractionsFromGpsTools();
        IntStream.range(0, InternalTestHelper.getInternalUserNumber())
                .forEach(i -> {
                    String userName = "internalUser" + i;
                    String phone = "000";
                    String email = userName + "@tourGuide.com";
                    User user = new User(UUID.randomUUID(), userName, phone,
                            email);
                    generateUserLocationHistory(user, attractions);
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
     * @param attractions
     */
    private void generateUserLocationHistory(final User user,
            final List<Attraction> attractions) {
        IntStream.range(0, NUMBER_OF_USER_VISITED_LOCATIONS_TO_CREATE)
                .forEach(i -> {
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
        double leftLimit = LONGITUDE_WEST_LIMIT;
        double rightLimit = LONGITUDE_EAST_LIMIT;
        return leftLimit + RANDOM.nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * This method return a randomized valid latitude.
     *
     * @return a double
     */
    private double generateRandomLatitude() {
        double leftLimit = LATITUDE_NORTH_LIMIT;
        double rightLimit = LATITUDE_SOUTH_LIMIT;
        return leftLimit + RANDOM.nextDouble() * (rightLimit - leftLimit);
    }

    /**
     * This method return a randomized LocalDateTime .
     *
     * @return a LocalDateTime
     */
    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now()
                .minusDays(RANDOM.nextInt(THIRTY));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    /**
     * Getter of Tracker.
     *
     * @return a Tracker
     */
    public Tracker getTracker() {
        return tracker;
    }

    /**
     * Getter of attractions, that is a class variable create to avoid many
     * calls to GpsUtil.
     *
     * @return a List<Attraction>
     */
    public List<Attraction> getAttractions() {
        return attractions;
    }

    /**
     * Setter of attractions.
     *
     * @param pAttractions
     */
    public void setAttractions(List<Attraction> pAttractions) {
        attractions = pAttractions;
    }

    
}
