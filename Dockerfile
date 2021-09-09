FROM openjdk:8-jdk AS builder
WORKDIR /builder
COPY . .
RUN ./gradlew installDist

FROM openjdk:8-jdk
RUN mkdir /app
COPY --from=builder /builder/build/install/KotlinTelegramBot/ /app/
WORKDIR /app/bin
CMD ["./KotlinTelegramBot"]