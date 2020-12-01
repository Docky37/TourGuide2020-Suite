# TourGuide2020 Suite - v2.1.2 release (01/12/2020)

## Overview:
TourGuide is a Spring Boot application of TripMaster's applications. It allows users to discover attractions near of their location and provides them discounts on hotel stays and reductions on ticket prices for shows.

Following its success, in 2020 TourGuide is faltering and suffers of poor performances, so TripMaster Group manager decided to develop this new version that will be deployed soon.


## Technical

TourGuide2020 is now a suite of 4 SpringBoot micro-services:

1. **TourGuideV2** is the main part of the suite and use 3 other autonomous Spring applications.
2. **GpsTool** application is its first collaborator. Based on GpsUtil.jar, it provides 2 end-points that allows TourGuideV2 to request the location of a user, and the list of attractions.
3. **GetRewards** application, based on RewardsCentral jar, is the second one. Its principal job is to calculate user's reward.
4. **TripDeals**, the last one, is powered by TripPricer jar and is used to find and provide opportunities of travels at good price. 

- All applications are build with SpringBoot v2.3.4 and Gradle 6.7. 
- The requests between TourGuide and other API are done with WebClient interfaces.
- JUnit5 manages the tests and MockWebServer is used to mock the WebClient. 
- Jacoco unit tests covers more than 80% of code. 
- Checkstyle and SpotBugs checks have been made.
- Swagger v3.0 provide documentation.


## Content

The current v2.1.2 release (01/12/2020) optimizes tracking and getRewards to boost performance with a non-blocking implementation of WebClient (Spring WebFlux - Reactive programming)

Previously in TourGuide2020:  

- The v2.0.beta_release (03 Nov. 2020) is the first release of the new architecture of TourGuide.  

- The v2.0.1.beta_release (08 Nov. 2020) optimizes the performances with less than 3 minutes spent to localize 100,000 users and less than 7 minutes to calculate 100,000 users' rewards.

- The v2.0.2.beta_release (20 Nov. 2020) finalizes functionalities, tests, code style and now runs in 4 Docker's containers.

- The v2.1.1 release (23 Nov. 2020) add UserNotFoundException, some tests and implements SpotBugs reports.

## Quick way to run with Docker
The folder 'QuickWayToRunInDocker' contains a docker-compose.yml file that downloads the TourGuide2020-Suite images on my Docker Hub repository and runs the application.
Change directory to 'QuickWayToRunInDocker' and use the command line '**docker-compose up -d**'.

## Other way
Important: Now 2 profiles are available. The 'dev' profile is used to run applications into your IDE, the 'docker' profile is required to build the Docker image of TourGuideV2. Profile can be set in application.propertis file.
 
1. This repository is divided in 4 parts; each one contains a SpringBoot v2.4.0 application built on Gradle v6.7.1
2. The build.gradle file of each part allows you to export the bootJar file by running '**gradle bootJar**' command. Before to run it, check that you are in the application root directory.
3. Exported file is located in build/libs sub-folder and, for the moment, need to be renamed (respectively as gps, rewards, tripdeals & tourguidev2). *I will do my best to change that!*
4. At the root of each application you can find the Dockerfile that allows you to create a Docker image of the application with the Docker command  '**docker build -t imageName.**' with respectively gps, rewards, tripdeals and tourguide as imageName.
5. When the 4 images are created, change directory to the repository root, where you can find the docker-compose.yml file. 
Here the command '**docker-compose up -d**' will run the 4 containers. 
Warning: TourGuide depends on the 3 others application and is launched after them. But it is possible that TourGuide request gps before gps is ready and TourGuide can exit. Its easy to check that with Docker Desktop and easy to launch it again.
6. You are now ready to check all my job with the Postman request collection joined in the repository root folder.

## DOCUMENTATION

A Swagger (v3.0) documentation is available at http://localhost:8080/swagger-ui/index.html#/
The Swagger UI allows you to tests end-points directly from the documentation. 

If you wanna test this endpoint, I think it will be helpfull for you to know the userName of all the 100 users created for test, isn't it ?
Don't worry, easy to remember that the first one is 'internalUser0' and the latest one 'internalUser99'!

Hope you enjoy it. Thanks.
   
 