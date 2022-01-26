package consts

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.http.*

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
        acceptContentTypes = acceptContentTypes + ContentType("application", "json+hal")
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.BODY
    }
    expectSuccess = false
}