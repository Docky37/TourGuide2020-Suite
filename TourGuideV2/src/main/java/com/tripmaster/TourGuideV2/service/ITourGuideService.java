package com.tripmaster.TourGuideV2.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.ProviderDTO;
import com.tripmaster.TourGuideV2.dto.UserPreferencesDTO;
import com.tripmaster.TourGuideV2.dto.UserRewardsDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;

@Service
public interface ITourGuideService {

    /**
     * This attribute defines the number of nearby attraction that will be
     * suggested.
     */
    int SIZE_OF_NEARBY_ATTRACTIONS_LIST = 5;

    /**
     * This method calls the UserRewards getter of User class.
     *
     * @param user
     * @return a List<UserReward>
     */
    UserRewardsDTO getUserRewards(User user);

    /**
     * This method get the user Location.
     *
     * @param user
     * @return a VisitedLocation
     */
    VisitedLocationDTO getUserLocation(User user);

    /**
     * Get a user (of the internalUserMap) by his userName.
     *
     * @param userName
     * @return a User
     */
    User getUser(String userName);

    /**
     * Get all users of the internalUserMap.
     *
     * @return a List of Users
     */
    List<User> getAllUsers();

    /**
     * This method is used to add a new user to the internalUserMap after
     * checking if he is not already registered.
     *
     * @param user
     */
    void addUser(User user);

    /**
     * This method use the TripPricer API to provide trip deals based on the
     * given user preferences.
     *
     * @param user
     * @return a List<Provider>
     */
    List<ProviderDTO> getTripDeals(User user);

    /**
     * Generates a new VisitedLocation (based on User location) and calculates a
     * Reward for the given user.
     *
     * @param user
     * @return a VisitedLocationDTO
     */
    VisitedLocationDTO trackUserLocation(User user);

    /**
     * This method build the requested Data Transfer Object from the list of the
     * closest tourist attractions to the user that is returned by the
     * getNearByAttractions sub method.
     *
     * @param user
     * @return an AttractionsSuggestionDTO that contains the user location, and
     *         a Map with attractionName as String key and a NearbyAttractionDTO
     *         as value.
     */
    AttractionsSuggestionDTO getAttractionsSuggestion(User user);

    /**
     * Get the list of all attractions from GpsTools microservice.
     *
     * @return a List<Attraction>
     */
    List<Attraction> getAllAttractionsFromGpsTools();

    /**
     * Get the list of the n closest attractions. The number n is defined by the
     * SIZE_OF_NEARBY_ATTRACTIONS_LIST constant.
     *
     * @param visitedLocation
     * @return a List<Attraction>
     */
    List<Attraction> getNearByAttractions(
            VisitedLocation visitedLocation);

    /**
     * This method get the Location of all users.
     *
     * @return a List<Location>
     */
    Map<String, LocationDTO> getAllUsersLocation();

    /**
     * This method allows user to update his preferences.
     *
     * @param user
     * @param userNewPreferencesDTO
     * @return a UserPreferencesDTO
     */
    UserPreferencesDTO updateUserPreferences(User user,
            UserPreferencesDTO userNewPreferencesDTO);

    /**
     * This method gets userPreferences from User.
     *
     * @param user
     * @return a UserPreferencesDTO
     */
    UserPreferencesDTO getPreferences(User user);

    /**
     * This method is used to add a new VisitedLocation.
     *
     * @param timeVisited
     * @param latitude
     * @param longitude
     * @param user
     * @return a VisitedLocationDTO
     */
    VisitedLocationDTO addVisitedLocation(Date timeVisited, double latitude,
            double longitude, User user);

}
