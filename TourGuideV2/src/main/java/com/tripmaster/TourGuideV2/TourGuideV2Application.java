package com.tripmaster.TourGuideV2;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * This class contains the main method used to run the application.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
@SpringBootApplication
public class TourGuideV2Application {

    /**
     * Main method that provides the entry point of this Spring application.
     *
     * @param args
     */
    public static void main(final String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(TourGuideV2Application.class, args);
    }

    /**
     * Empty class constructor.
     */
    protected TourGuideV2Application() {
    }
    /**
     * Customize Swagger2 documentation, skipping ErrorController and adding
     * model classes.
     *
     * @return a Docket object
     */
    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .paths(PathSelectors.regex("(?!/error.*).*"))
                .apis(RequestHandlerSelectors
                        .basePackage("com.tripmaster.TourGuideV2"))
                .build().apiInfo(infoDetails());
    }

    private ApiInfo infoDetails() {
        return new ApiInfo("Tripmaster - Tour Guide 2020 Suite",
                "What's a wonderful world!", "v2.1", "Free to use",
                new springfox.documentation.service.Contact("Thierry Schreiner",
                        "http://doc.tripmaster.com/tourguide",
                        "DockyDocs@tripmaster.com"),
                "API License", "http://tripmaster.com",
                Collections.emptyList());
    }

}
