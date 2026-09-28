FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre
RUN adduser --system spring-server && addgroup --system spring-server && adduser spring-server spring-server
USER spring-server
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["sh", "-c", "exec java -XX:HeapDumpPath=/dumps/heap-$(date +%Y%m%d-%H%M%S).hprof -jar app.jar"]