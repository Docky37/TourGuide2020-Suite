FROM openjdk:11 AS BUILD_IMAGE
ENV APP_HOME=/root/TourGuide2020-Suite/GetRewards
RUN mkdir -p $APP_HOME/src/main/java
WORKDIR $APP_HOME
COPY build.gradle gradlew gradlew.bat $APP_HOME
COPY gradle $APP_HOME/gradle
# download dependencies
RUN ./gradlew build -x :bootJar -x test --continue
COPY . .
RUN ./gradlew build
FROM openjdk:11-jre
WORKDIR /root/
COPY --from=BUILD_IMAGE /root/TourGuide2020-Suite/GetRewards/build/libs/rewards.jar .
EXPOSE 8787
CMD ["java","-jar","rewards.jar"]
