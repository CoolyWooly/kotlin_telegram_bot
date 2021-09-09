FROM openjdk:8-jdk
RUN mkdir /app
COPY ./build/install/KotlinTelegramBot/ /app/
WORKDIR /app/bin
CMD ["./KotlinTelegramBot"]