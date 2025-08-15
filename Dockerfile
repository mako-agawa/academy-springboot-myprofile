FROM eclipse-temurin:17
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew bootJar -x test && \
    cp build/libs/*.jar app.jar
CMD ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]