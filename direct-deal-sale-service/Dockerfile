FROM adoptopenjdk/openjdk11:alpine-jre
LABEL image.authors="Jeon, Cheol"
ADD build/libs/direct-deal-sale-service-0.1.jar .
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","direct-deal-sale-service-0.1.jar"]
