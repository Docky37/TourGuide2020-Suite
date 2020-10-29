package com.tripmaster.TourGuideV2.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tripmaster.TourGuideV2.domain.Attraction;
import com.tripmaster.TourGuideV2.domain.User;
import com.tripmaster.TourGuideV2.domain.UserReward;
import com.tripmaster.TourGuideV2.domain.VisitedLocation;
import com.tripmaster.TourGuideV2.dto.AttractionsSuggestionDTO;
import com.tripmaster.TourGuideV2.dto.LocationDTO;
import com.tripmaster.TourGuideV2.dto.VisitedLocationDTO;

@Service
public interface ITourGuideService {

    int SIZE_OF_NEARBY_ATTRACTIONS_LIST = 5;

    /**
     * This method calls the UserRewards getter of User class.
     *
     * @param user
     * @return a List<UserReward>
     */
    List<UserReward> getUserRewards(User user);

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
     * @return
     */
    User getUser(String userName);

    /**
     * Get all users of the internalUserMap.
     *
     * @return
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
    //List<Provider> getTripDeals(User user);

    /**
     * Generates a new VisitedLocation (based on User location) and calculates a
     * Reward for the given user.
     *
     * @param user
     * @return
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

    List<Attraction> getAllAttractions();
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

}