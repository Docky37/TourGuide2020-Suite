package com.tripmaster.TourGuideV2.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import gpsUtil.location.VisitedLocation;
//import tripPricer.Provider;

/**
 * The domain class User contains all data of TourGuide users.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class User {
    private final UUID userId;
    private final String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private List<VisitedLocation> visitedLocations = new ArrayList<>();
    private List<UserReward> userRewards = new ArrayList<>();
    private UserPreferences userPreferences = new UserPreferences();
    //private List<Provider> tripDeals = new ArrayList<>();

    /**
     * Class constructor.
     *
     * @param userId
     * @param userName
     * @param phoneNumber
     * @param emailAddress
     */
    public User(UUID userId, String userName, String phoneNumber,
            String emailAddress)
    {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * Getter of UserId.
     *
     * @return a UUID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Getter of UserName.
     *
     * @return a String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter of phoneNumber.
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Getter of phoneNumber.
     *
     * @return a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter of emailAddress.
     *
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Getter of emailAddress.
     *
     * @return a String
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Setter of latestLocationTimestamp.
     *
     * @param latestLocationTimestamp
     */
    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    /**
     * Getter of latestLocationTimestamp.
     *
     * @return a Date
     */
    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    /**
     * Adds the given visitedLocation to the visitedLocations List.
     *
     * @param visitedLocation
     */
    public void addToVisitedLocations(VisitedLocation visitedLocation) {
        visitedLocations.add(visitedLocation);
    }

    /**
     * Getter of the visitedLocations List.
     *
     * @return
     */
    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    /**
     * Removes all of the elements from the visitedLocations list.
     */
    public void clearVisitedLocations() {
        visitedLocations.clear();
    }

    /**
     * Adds the given userReward to the userRewards List.
     *
     * @param userReward
     */
    public void addUserReward(UserReward userReward) {
        userRewards.add(userReward);
    }

    /**
     * Getter of the userRewards List.
     *
     * @return a List<UserReward>
     */
    public List<UserReward> getUserRewards() {
        return userRewards;
    }

    /**
     * Getter of userPreferences.
     *
     * @return a UserPreferences object
     */
    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    /**
     * Setter of userPreferences.
     *
     * @param userPreferences
     */
    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    /**
     * Return the latest visitedLocation.
     *
     * @return a VisitedLocation object
     */
    public VisitedLocation getLastVisitedLocation() {
        return visitedLocations.get(visitedLocations.size() - 1);
    }

    /**
     * Setter of the tripDeals list.
     *
     * @param tripDeals
     */
    /*public void setTripDeals(List<Provider> tripDeals) {
        this.tripDeals = tripDeals;
    }*/

    /**
     * Getter of the tripDeals list.
     *
     * @return a List<Provider>
     */
    /*public List<Provider> getTripDeals() {
        return tripDeals;
    }*/

    /**
     * Used by the following toString method for the concatenation of the
     * visitedLocations List.
     */
    //private String serializedVisitedLocations = "";

    /**
     * Serialization method.
     */
    /*@Override
    public String toString() {
        visitedLocations
                .forEach(l -> this.serializedVisitedLocations += " {lat="
                        + l.location.latitude
                        + ", long=" + l.location.longitude + ", "
                        + l.timeVisited.toString()
                        + "}");

        return "User [userId=" + userId + ", userName=" + userName
                + ", phoneNumber=" + phoneNumber + ", emailAddress="
                + emailAddress + ", latestLocationTimestamp="
                + latestLocationTimestamp + ", visitedLocations= ["
                + serializedVisitedLocations + "], userRewards="
                + userRewards.toString()
                + ", userPreferences=" + userPreferences.toString()
                + ", tripDeals=" + tripDeals + "]";
    }*/

}
