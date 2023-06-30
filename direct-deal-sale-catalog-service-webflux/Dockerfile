FROM adoptopenjdk/openjdk11:alpine-jre
LABEL image.authors="Jeon, Cheol"
ADD build/libs/direct-deal-sale-catalog-service-webflux-0.1.jar .
EXPOSE 8082
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","direct-deal-sale-catalog-service-webflux-0.1.jar"]
