package com.tripmaster.TourGuideV2.dto;

/**
 * This Data Transfer Object is used to transfer user's preferences.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class UserPreferencesDTO {

    /**
     * The radius of the area where attractions is considered as nearby one by
     * the user.
     */
    private int attractionProximity = Integer.MAX_VALUE;

    /**
     * The minimum price value of a TripDeal.
     */
    private int lowerPricePoint = 0;

    /**
     * The maximum price value of a TripDeal.
     */
    private int highPricePoint = Integer.MAX_VALUE;

    /**
     * The favorite duration of trip the user is looking for.
     */
    private int tripDuration = 1;

    /**
     * The quantity of ticket.
     */
    private int ticketQuantity = 1;

    /**
     * The number of adults that will participate to a trip.
     */
    private int numberOfAdults = 1;

    /**
     * The number of children that will participate to a trip.
     */
    private int numberOfChildren = 0;

    /**
     * Empty class constructor.
     */
    public UserPreferencesDTO() {
    }

    /**
     * Setter of attractionProximity.
     *
     * @param pAttractionProximity
     */
    public void setAttractionProximity(final int pAttractionProximity) {
        attractionProximity = pAttractionProximity;
    }

    /**
     * Getter of attractionProximity.
     *
     * @return an int
     */
    public int getAttractionProximity() {
        return attractionProximity;
    }

    /**
     * Getter of lowerPricePoint.
     *
     * @return an int
     */
    public int getLowerPricePoint() {
        return lowerPricePoint;
    }

    /**
     * Setter of lowerPricePoint.
     *
     * @param pLowerPricePoint
     */
    public void setLowerPricePoint(final int pLowerPricePoint) {
        lowerPricePoint = pLowerPricePoint;
    }

    /**
     * Getter of highPricePoint.
     *
     * @return an int
     */
    public int getHighPricePoint() {
        return highPricePoint;
    }

    /**
     * Setter of highPricePoint.
     *
     * @param pHighPricePoint
     */
    public void setHighPricePoint(final int pHighPricePoint) {
        highPricePoint = pHighPricePoint;
    }

    /**
     * Getter of tripDuration.
     *
     * @return an int
     */
    public int getTripDuration() {
        return tripDuration;
    }

    /**
     * Setter of tripDuration.
     *
     * @param pTripDuration
     */
    public void setTripDuration(final int pTripDuration) {
        tripDuration = pTripDuration;
    }

    /**
     * Getter of ticketQuantity.
     *
     * @return an int
     */
    public int getTicketQuantity() {
        return ticketQuantity;
    }

    /**
     * Setter of ticketQuantity.
     *
     * @param pTicketQuantity
     */
    public void setTicketQuantity(final int pTicketQuantity) {
        ticketQuantity = pTicketQuantity;
    }

    /**
     * Getter of numberOfAdults.
     *
     * @return an int
     */
    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    /**
     * Setter of numberOfAdults.
     *
     * @param pNumberOfAdults
     */
    public void setNumberOfAdults(final int pNumberOfAdults) {
        numberOfAdults = pNumberOfAdults;
    }

    /**
     * Getter of numberOfChildren.
     *
     * @return an int
     */
    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    /**
     * Setter of numberOfChildren.
     *
     * @param pNumberOfChildren
     */
    public void setNumberOfChildren(final int pNumberOfChildren) {
        numberOfChildren = pNumberOfChildren;
    }

}
