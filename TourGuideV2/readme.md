# TourGuide2020 Suite - v2.0 Beta release

## Overview:
TourGuide is a Spring Boot application of TripMaster's applications. It allows users to discover attractions in the vicinity of their location and provides them discounts on hotel stays and reductions on ticket prices for shows.

Following its success, in 2020 TourGuide is faltering and suffers of poor performances, so TripMaster Group manager decided to develop a this new version that will be deployed soon.

## Technical

TourGuideServi2020 is now a suite of 4 SpringBoot applications:

1. **TourGuideV2** is the main part of the suite and use 3 other autonomous Spring applications
2. **GpsTool** application is its first collaborator. Based on GpsUtil.jar, it provides 2 end-points that allows TourGuideV2 to request the location of a user, and the list of attractions.
3. **GetRewards** application, based on RewardsCentral jar, is the second one. Its principal job is to calculate user's reward.
4. **TripDeals**, the last one, is powered by TripPricer jar and is used to find and provide opportunities of travels at good price. 

All applications are build with SpringBoot v2.3.4 and Gradle.
The requests between TourGuide and other API are done with WebClient.
JUnit 5 manages the tests and MockWebServer is used to mock the WebClient. 

## Content

The v2.0.beta_release (03 Nov. 2020) is the first release of the new architecture of TourGuide.  

The v2.0.1.beta_version (08 Nov. 2020) optimizes the performances with less than 3 minutes spent to localize 100,000 users and less than 7 minutes to calculate 100,000 users' rewards. 