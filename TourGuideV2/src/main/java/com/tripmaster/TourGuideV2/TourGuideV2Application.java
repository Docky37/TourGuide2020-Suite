package com.tripmaster.TourGuideV2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TourGuideV2Application {

	public static void main(String[] args) {
	    System.setProperty("spring.devtools.restart.enabled", "false");
	    SpringApplication.run(TourGuideV2Application.class, args);
	}

}
