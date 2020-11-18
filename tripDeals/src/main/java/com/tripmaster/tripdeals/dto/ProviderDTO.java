package com.tripmaster.tripdeals.dto;

import java.util.UUID;

public class ProviderDTO {

    /**
     * The id of this provided trip.
     */
    private UUID tripId;

    /**
     * The name of this provided trip.
     */
    private String name;

    /**
     * The price of this provided trip.
     */
    private double price;

    /**
     * All parameters class constructor.
     *
     * @param pTripId
     * @param pName
     * @param pPrice
     */
    public ProviderDTO(final UUID pTripId, final String pName,
            final double pPrice) {
        tripId = pTripId;
        name = pName;
        price = pPrice;
    }

    /**
     * No parameters protected class constructor.
     */
    protected ProviderDTO() {
    }

    /**
     * Getter of the tridId attribute.
     *
     * @return an UUID
     */
    public UUID getTripId() {
        return tripId;
    }

    /**
     * Getter of the name attribute.
     *
     * @return a String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of the price attribute.
     *
     * @return a double
     */
    public double getPrice() {
        return price;
    }

}
