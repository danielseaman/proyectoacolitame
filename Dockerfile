FROM openjdk:8-jdk-slim
COPY "./target/proyectoacolitame-0.0.1-SNAPSHOT.jar" "app.jar"
COPY "publicKey.pem" "publicKey.pem"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]