FROM adoptopenjdk/openjdk11:alpine-jre
LABEL image.authors="Jeon, Cheol"
ADD build/libs/direct-deal-account-service-0.1.jar .
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","direct-deal-account-service-0.1.jar"]
