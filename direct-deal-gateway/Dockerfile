FROM adoptopenjdk/openjdk11:alpine-jre
LABEL image.authors="Jeon, Cheol <real.overcomer.jc@gmail.com>"
ADD build/libs/direct-deal-gateway-0.1.jar .
EXPOSE 8084
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","direct-deal-gateway-0.1.jar"]