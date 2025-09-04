FROM alpine/java:17.0.9-jre
LABEL image.authors="Jeon, Cheol"
ADD build/libs/direct-deal-sale-catalog-service-webflux-0.1.jar .
EXPOSE 8082
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","direct-deal-sale-catalog-service-webflux-0.1.jar"]
