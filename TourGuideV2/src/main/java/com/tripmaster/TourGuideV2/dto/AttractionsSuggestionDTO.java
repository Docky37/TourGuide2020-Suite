package com.tripmaster.TourGuideV2.dto;

import java.util.Map;


public class AttractionsSuggestionDTO {

    /**
     * The latest position of the user.
     */
    private LocationDTO userLocation;

    /**
     * A Map collection of nearest suggested attractions.
     */
    private Map<String, NearbyAttractionDTO> suggestedAttractions;

    /**
     * Empty class constructor.
     */
    public AttractionsSuggestionDTO() {
    }

    /**
     * Getter of userLocation.
     *
     * @return a Location (latitude and longitude)
     */
    public LocationDTO getUserLocation() {
        return userLocation;
    }

    /**
     * Setter of userLocation.
     *
     * @param userLocation
     */
    public void setUserLocation(LocationDTO userLocation) {
        this.userLocation = userLocation;
    }

    /**
     * Getter of suggestedAttraction.
     *
     * @return a Map<String, NearbyAttractionsDTO> with the attraction name as
     *         key, and the NearbyAttractionsDTO (location, distance,
     *         userReward) as value.
     */
    public Map<String, NearbyAttractionDTO> getSuggestedAttraction() {
        return suggestedAttractions;
    }

    /**
     * Setter of suggestedAttraction.
     *
     * @param suggestedAttraction
     */
    public void setSuggestedAttractions(
            Map<String, NearbyAttractionDTO> suggestedAttractions) {
        this.suggestedAttractions = suggestedAttractions;
    }

    @Override
    public String toString() {
        return "AttractionsSuggestionDTO [userLocation=" + userLocation
                + ", suggestedAttractions=" + suggestedAttractions + "]";
    }

}
