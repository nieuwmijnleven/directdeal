FROM adoptopenjdk/openjdk11:alpine-jre
LABEL image.authors="Jeon, Cheol <real.overcomer.jc@gmail.com>"
ADD build/libs/direct-deal-transaction-history-service-0.1.jar .
EXPOSE 8083
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","direct-deal-transaction-history-service-0.1.jar"]