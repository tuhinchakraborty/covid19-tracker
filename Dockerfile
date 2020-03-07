# Start with a base image containing Java runtime
FROM openjdk:13-jdk-alpine

# Add Maintainer Info
LABEL maintainer="tuhin_chakraborty@outlook.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=/build/libs/covid19-tracker-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} covid19-tracker-0.0.1-SNAPSHOT.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/covid19-tracker-0.0.1-SNAPSHOT.jar"]
