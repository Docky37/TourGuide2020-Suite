package com.tripmaster.gps.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tripmaster.gps.dto.AttractionDTO;
import com.tripmaster.gps.dto.LocationDTO;
import com.tripmaster.gps.dto.VisitedLocationDTO;
import com.tripmaster.gps.service.IGpsService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GpsControllerTest {

    
    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IGpsService gpsService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Given a userId when getLocation then call getLocationService method ")
    public void givenAUserId_whenGetLocation_thenCallGetLocationServiceMethod() throws Exception {
        // GIVEN
        UUID userId = UUID.randomUUID();
        given(gpsService.getUserLocation(userId))
                .willReturn(new VisitedLocationDTO(new LocationDTO(45.0, 1.0),
                        new Date(), userId));
        // WHEN
        mvc.perform(
                MockMvcRequestBuilders.get("/getUserLocation?userId=" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(gpsService).getUserLocation(userId);
    }

    @Test
    @DisplayName("When request getAllAttractions then call service getAttractions method")
    public void whenGetAllAttractions_thenCallsServiceGetAttractionsMethod()
            throws Exception {
        // GIVEN
        List<AttractionDTO> attractions = new ArrayList<>();
        given(gpsService.getAttractions()).willReturn(attractions);
        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/getAllAttractions")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(gpsService).getAttractions();
    }

}
