package com.tripmaster.TourGuideV2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class TourGuideModule {
	
	@Bean
	public WebClient getWebClientTripDeals() {
		return WebClient.create("http://localhost:8888");
	}
	
	@Bean
	public WebClient getWebClientGps() {
		return WebClient.create("http://localhost:8889");
	}
	
	@Bean
	public WebClient getWebClientReward() {
		return WebClient.create("http://localhost:8787");
	}
	
}
