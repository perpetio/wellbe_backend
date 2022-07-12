#Build shadow jar

#Get gradle:7-jdk11-alpine from docker hub
FROM gradle:7-jdk11-alpine AS build
#First argument host machine second to image filesystem
COPY --chown=gradle:gradle . /home/gradle/src
#Set the directory for executing future commands
WORKDIR /home/gradle/src
#Command for image file system
RUN gradle shadowJar --no-daemon

#Run shadow jar file

#Get openjdk:11 from docker hub
FROM openjdk:11
EXPOSE 8082:8082
#Command for image file system
RUN mkdir /app
#First argument host machine second to image filesystem
COPY --from=build /home/gradle/src/build/libs/com.perpetio.ktor-chat-0.0.1-all.jar /app/com.perpetio.ktor-chat-0.0.1-all.jar
ENTRYPOINT ["java","-jar","/app/com.perpetio.ktor-chat-0.0.1-all.jar"]