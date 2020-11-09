package com.tripmaster.TourGuideV2.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.tripmaster.TourGuideV2.dto.ProviderDTO;

//import gpsUtil.location.VisitedLocation;
//import tripPricer.Provider;

/**
 * The domain class User contains all data of TourGuide users.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class User {

    /**
     * The id (an UUID) of the User.
     */
    private final UUID userId;
    /**
     * The user name of this User.
     */
    private final String userName;
    /**
     * The phone number of this User.
     */
    private String phoneNumber;
    /**
     * The emailAddress of this User.
     */
    private String emailAddress;
    /**
     * The date of the last visitedLocation data entry.
     */
    private Date latestLocationTimestamp;
    /**
     * The List of all user's visitedLocations.
     */
    private List<VisitedLocation> visitedLocations = new ArrayList<>();
    /**
     * The List of all user's rewards.
     */
    private List<UserReward> userRewards = new ArrayList<>();
    /**
     * The date of the last visitedLocation data entry.
     */
    private UserPreferences userPreferences = new UserPreferences();
    /**
     * The List of Trip deals propositions (ProviderDTO).
     */
    private List<ProviderDTO> tripDeals = new ArrayList<>();

    /**
     * All parameters Class constructor.
     *
     * @param pUserId
     * @param pUserName
     * @param pPhoneNumber
     * @param pEmailAddress
     */
    public User(final UUID pUserId, final String pUserName,
            final String pPhoneNumber, final String pEmailAddress) {
        this.userId = pUserId;
        this.userName = pUserName;
        this.phoneNumber = pPhoneNumber;
        this.emailAddress = pEmailAddress;
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
     * Getter of phoneNumber.
     *
     * @return a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter of phoneNumber.
     *
     * @param pPhoneNumber
     */
    public void setPhoneNumber(final String pPhoneNumber) {
        phoneNumber = pPhoneNumber;
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
     * Setter of emailAddress.
     *
     * @param pEmailAddress
     */
    public void setEmailAddress(final String pEmailAddress) {
        emailAddress = pEmailAddress;
    }

    /**
     * Setter of latestLocationTimestamp.
     *
     * @param pLatestLocationTimestamp
     */
    private void setLatestLocationTimestamp(
            final Date pLatestLocationTimestamp) {
        latestLocationTimestamp = (Date) pLatestLocationTimestamp.clone();
    }

    /**
     * Getter of latestLocationTimestamp.
     *
     * @return a Date
     */
    public Date getLatestLocationTimestamp() {
        return (Date) latestLocationTimestamp.clone();
    }

    /**
     * Adds the given visitedLocation to the visitedLocations List.
     *
     * @param visitedLocation
     */
    public void addToVisitedLocations(final VisitedLocation visitedLocation) {
        visitedLocations.add(visitedLocation);
        setLatestLocationTimestamp(visitedLocation.getTimeVisited());
    }

    /**
     * Getter of the visitedLocations List.
     *
     * @return a List<VisitedLocation>
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
    public void addUserReward(final UserReward userReward) {
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
     * @param pUserPreferences
     */
    public void setUserPreferences(final UserPreferences pUserPreferences) {
        userPreferences = pUserPreferences;
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
     * @param pTripDeals
     */
    public void setTripDeals(final List<ProviderDTO> pTripDeals) {
        tripDeals = pTripDeals;
    }

    /**
     * Getter of the tripDeals list.
     *
     * @return a List<Provider>
     */
    public List<ProviderDTO> getTripDeals() {
        return tripDeals;
    }

    /**
     * Used by the following toString method for the concatenation of the
     * visitedLocations List.
     */
    private String serializedVisitedLocations = "";

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        visitedLocations
                .forEach(l -> this.serializedVisitedLocations += " {lat="
                        + l.getLocation().getLatitude()
                        + ", long=" + l.getLocation().getLongitude() + ", "
                        + l.getTimeVisited().toString()
                        + "}");

        return "User [userId=" + userId + ", userName=" + userName
                + ", phoneNumber=" + phoneNumber + ", emailAddress="
                + emailAddress + ", latestLocationTimestamp="
                + latestLocationTimestamp + ", visitedLocations= ["
                + serializedVisitedLocations + "], userRewards="
                + userRewards.toString()
                + ", userPreferences=" + userPreferences.toString()
                + ", tripDeals=" + tripDeals + "]";
    }

}
