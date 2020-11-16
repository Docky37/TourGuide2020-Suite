package com.tripmaster.TourGuideV2.dto;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;

/**
 * This domain class is used to store all user's preferences.
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
     * Getter of currency.
     *
     * @return a CurrencyUnit
     */
    public CurrencyUnit getCurrency() {
        return currency;
    }

    /**
     * Setter of currency.
     *
     * @param pCurrency
     */
    public void setCurrency(final CurrencyUnit pCurrency) {
        currency = pCurrency;
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
     * @param pLowerPricePoint
     */
    public void setLowerPricePoint(final Money pLowerPricePoint) {
        lowerPricePoint = pLowerPricePoint;
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
     * @param pHighPricePoint
     */
    public void setHighPricePoint(final Money pHighPricePoint) {
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

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "UserPreferencesDTO [attractionProximity=" + attractionProximity
                + ", currency=" + currency + ", lowerPricePoint="
                + lowerPricePoint + ", highPricePoint=" + highPricePoint
                + ", tripDuration=" + tripDuration + ", ticketQuantity="
                + ticketQuantity + ", numberOfAdults=" + numberOfAdults
                + ", numberOfChildren=" + numberOfChildren + "]";
    }

}
