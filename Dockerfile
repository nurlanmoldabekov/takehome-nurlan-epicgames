FROM amazoncorretto:17.0.5-alpine
MAINTAINER epicgames.com
COPY build/libs/takehome-0.0.1-SNAPSHOT.jar takehome-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/takehome-0.0.1-SNAPSHOT.jar"]