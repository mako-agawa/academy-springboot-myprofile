FROM eclipse-temurin:17
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew build -x test
CMD ["java", "-Dserver.port=${PORT}", "-jar", "build/libs/*-SNAPSHOT.jar"]