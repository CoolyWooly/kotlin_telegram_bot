import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.yerlan"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    //ktor
    implementation ("io.ktor:ktor-client-core:1.6.3")
    implementation("io.ktor:ktor-client-gson:1.6.3")
    implementation ("io.ktor:ktor-client-cio:1.6.3")
    //ktor logging
    implementation("ch.qos.logback:logback-classic:1.2.5")
    implementation("io.ktor:ktor-client-logging:1.6.3")
    //telegram bot
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.5")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.create("stage") {
    dependsOn("installDist")
}

application {
    mainClass.set("ServerKt")
}