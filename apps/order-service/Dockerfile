# 1단계: 빌드 스테이지
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

COPY order-service/build.gradle ./order-service/
RUN mkdir payment-service && touch payment-service/build.gradle

RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

COPY order-service/src ./order-service/src
RUN ./gradlew :order-service:bootJar -x test --no-daemon

# 2단계: 실행 스테이지
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

COPY --from=build /workspace/app/order-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]