FROM alpine/java:17.0.9-jre
LABEL image.authors="Jeon, Cheol"
ADD build/libs/direct-deal-chatting-service-0.1.jar .
EXPOSE 8085
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","direct-deal-chatting-service-0.1.jar"]
