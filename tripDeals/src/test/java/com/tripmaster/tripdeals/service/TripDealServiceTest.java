package com.tripmaster.tripdeals.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tripmaster.tripdeals.dto.ProviderDTO;

import tripPricer.Provider;
import tripPricer.TripPricer;

@ExtendWith(SpringExtension.class)
public class TripDealServiceTest {

    @Mock
    TripPricer tripPricer;

    @InjectMocks
    ITripDealService tripDealService = new TripDealService(tripPricer);

    static String tripPricerApiKey = "test-server-api-key";

    @Test
    public void given_when_then() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        List<Provider> providerList = new ArrayList<>();
        providerList.add(new Provider(UUID.randomUUID(),
                "Enterprize Ventures Limited", 157.50));
        providerList.add(new Provider(UUID.randomUUID(), "Live Free", 445.99));
        providerList.add(new Provider(UUID.randomUUID(),
                "United Partners Vacations", 266.99));
        providerList
                .add(new Provider(UUID.randomUUID(), "AdventureCo", 258.99));
        providerList
                .add(new Provider(UUID.randomUUID(), "Dream Trips", 430.59));
        given(tripPricer.getPrice(tripPricerApiKey, userId, 2, 2, 24, 523))
                .willReturn(providerList);
        // WHEN
        List<ProviderDTO> providerDTOList = tripDealService
                .getTripDeals(tripPricerApiKey, userId, 2, 2, 24, 523);
        // THEN
        verify(tripPricer).getPrice(tripPricerApiKey, userId, 2, 2, 24, 523);
        assertThat(providerDTOList.size()).isEqualTo(5);
    }

}
