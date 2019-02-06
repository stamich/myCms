FROM openjdk:8u191-jre-alpine
RUN apk --no-cache add url
CMD java ${JAVA_OPTS} -jar mycms-main.jar
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/actuator/health || exit 1
COPY build/libs/mycms-main.jar .