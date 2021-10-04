import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

val WRAP_API_KEY = "UqgqcWh6kN4BXG8H7rTS9LOvMNZECap5"
val CHAT_ID = 266637514L
val oldCarsList = arrayListOf<CarItemModel>()
val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
        acceptContentTypes = acceptContentTypes + ContentType("application", "json+hal")
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.BODY
    }
}
val bot = bot {
    token = "1970497958:AAEc6C8NdaHBJoAWtWtyma5eke7YU33tqno"
}

@ExperimentalTime
fun main() = runBlocking {
    while (isActive) {
        getLIst()
        delay(Duration.minutes(2))
    }
}

private suspend fun getLIst() {
    bot.sendMessage(ChatId.fromId(CHAT_ID), text = "Поиск авто...")

    val responseList: KolesaResponse<List<CarItemModel>> =
        client.get("https://wrapapi.com/use/CoolyWooly/kolesa/list/latest") {
            parameter("wrapAPIKey", WRAP_API_KEY)
            parameter("page", 1)
            parameter("city", "nur-sultan")
        }

    if (!responseList.data.isNullOrEmpty()) {
        responseList.data.forEach { newCar ->
            if (oldCarsList.find { oldCar -> oldCar.id == newCar.id } != null) return@forEach          // old car return
            getItem(newCar.id)
        }
        oldCarsList.clear()
        oldCarsList.addAll(responseList.data)
    }
}

private suspend fun getItem(id: String) {
    val responseItem: KolesaResponse<CarModel> =
        client.get("https://wrapapi.com/use/CoolyWooly/kolesa/item/latest") {
            parameter("wrapAPIKey", WRAP_API_KEY)
            parameter("id", id)
        }

    if (responseItem.data?.diff != null && responseItem.data.diff <= 0) {
        if (responseItem.data.title == null) return
        if (responseItem.data.photo == null) return

        val arrayText = arrayListOf<String>()
        responseItem.data.title.let { arrayText.add(it) }
        responseItem.data.probeg?.let { arrayText.add("Пробег: $it") }
        responseItem.data.getPriceStr().let { arrayText.add(it) }
        responseItem.data.diff.let { arrayText.add("$it%") }
        arrayText.add("https://kolesa.kz/a/show/$id")

        bot.sendPhoto(
            ChatId.fromId(CHAT_ID),
            photo = responseItem.data.photo,
            caption = arrayText.joinToString(separator = "\n")
        )
    }
}