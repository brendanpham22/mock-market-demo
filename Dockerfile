FROM openjdk:8

ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} product.jar

ENTRYPOINT ["java","-jar","product.jar"]