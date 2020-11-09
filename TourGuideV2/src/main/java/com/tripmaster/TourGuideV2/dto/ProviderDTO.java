package com.tripmaster.TourGuideV2.dto;

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
     * No parameters class constructor.
     */
    public ProviderDTO() {
    }

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

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "{\"tripId\":\"" + tripId + "\", \"name\":\"" + name
                + "\", price\":\""
                + price + "\"}";
    }

}
