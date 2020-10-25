package com.tripmaster.TourGuideV2.domain;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;

/**
 * This domain class is used to store all user's preferences.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class UserPreferences {

    /**
     * The radius of the area where attractions is considered as nearby by the
     * user.
     */
    private int attractionProximity = Integer.MAX_VALUE;

    /**
     * Attribute that defines US Dollar as used currency of the application.
     */
    private CurrencyUnit currency = Monetary.getCurrency("USD");

    /**
     * The minimum price value of a TripDeal.
     */
    private Money lowerPricePoint = Money.of(0, currency);

    /**
     * The maximum price value of a TripDeal.
     */
    private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);

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
     * Empty class constructor
     */
    public UserPreferences() {
    }

    /**
     * Setter of attractionProximity.
     *
     * @param attractionProximity
     */
    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
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
     * @return a Money object
     */
    public Money getLowerPricePoint() {
        return lowerPricePoint;
    }

    /**
     * Setter of lowerPricePoint.
     *
     * @param lowerPricePoint
     */
    public void setLowerPricePoint(Money lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    /**
     * Getter of highPricePoint.
     *
     * @return a Money object
     */
    public Money getHighPricePoint() {
        return highPricePoint;
    }

    /**
     * Setter of highPricePoint.
     *
     * @param highPricePoint
     */
    public void setHighPricePoint(Money highPricePoint) {
        this.highPricePoint = highPricePoint;
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
     * @param tripDuration
     */
    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    /**
     * Getter of ticketQuantity.
     *
     * @return
     */
    public int getTicketQuantity() {
        return ticketQuantity;
    }

    /**
     * Setter of ticketQuantity.
     *
     * @param ticketQuantity
     */
    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
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
     * @param numberOfAdults
     */
    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
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
     * @param numberOfChildren
     */
    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "UserPreferences [attractionProximity=" + attractionProximity
                + ", currency=" + currency + ", lowerPricePoint="
                + lowerPricePoint + ", highPricePoint=" + highPricePoint
                + ", tripDuration=" + tripDuration + ", ticketQuantity="
                + ticketQuantity + ", numberOfAdults=" + numberOfAdults
                + ", numberOfChildren=" + numberOfChildren + "]";
    }

}
