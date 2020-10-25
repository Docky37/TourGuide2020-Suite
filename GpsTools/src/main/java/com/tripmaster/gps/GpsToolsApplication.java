package com.tripmaster.gps;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GpsToolsApplication {

	public static void main(String[] args) {
        Locale.setDefault(Locale.US);
		SpringApplication.run(GpsToolsApplication.class, args);
	}

}
