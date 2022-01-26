import com.github.kotlintelegrambot.entities.ChatId
import com.google.gson.Gson
import consts.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import models.ItemResponse
import models.ListResponse
import models.PriceResponse
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = runBlocking {
    while (isActive) {
        getList()
        delay(Duration.minutes(10))
    }
}

private suspend fun getList() {
    val strOld = fileOldMessage.readText()
    // Удаляем Поиск авто...
    if (strOld.isNotEmpty()) {
        bot.deleteMessage(ChatId.fromId(CHAT_ID), messageId = strOld.toLong())
    }
    // Отправляем Поиск авто...
    val result = bot.sendMessage(ChatId.fromId(CHAT_ID), text = "Поиск авто...")
    fileOldMessage.writeText(result.first?.body()?.result?.messageId.toString())

    val list: ListResponse =
        client.get("https://wrapapi.com/use/CoolyWooly/kolesa/list/latest") {
            parameter("wrapAPIKey", WRAP_API_KEY)
            parameter("page", 1)
            parameter("city", "nur-sultan")
        }

    if (!list.data.isNullOrEmpty()) {
        val oldCarList = fileData.readLines()
        list.data.forEach { newCar ->
            if (oldCarList.contains(newCar.id)) return@forEach
            getItem(id = newCar.id, diff = list.diff)
        }
        list.data.forEachIndexed { index, model ->
            if (index == 0) {
                fileData.writeText(model.id)
            } else {
                fileData.appendText("\n")
                fileData.appendText(model.id)
            }
        }
    }
}

private suspend fun getItem(id: String, diff: Int) {
    val item: ItemResponse =
        client.get("https://wrapapi.com/use/CoolyWooly/kolesa/item/latest") {
            parameter("wrapAPIKey", WRAP_API_KEY)
            parameter("id", id)
        }

    val price: PriceResponse =
        client.get("https://kolesa.kz/a/average-price/$id")

    if (price.data?.diffInPercents != null && price.data.diffInPercents < diff) {
        if (item.data?.title == null) return
        if (item.data.photo == null) return

        val arrayText = arrayListOf<String>()
        item.data.title.let { arrayText.add(it) }
        item.data.probeg?.let { arrayText.add("Пробег: $it") }
        item.data.getPriceStr().let { arrayText.add(it) }
        price.data.diffInPercents.let { arrayText.add("$it%") }
        arrayText.add("https://kolesa.kz/a/show/$id")

        bot.sendPhoto(
            ChatId.fromId(CHAT_ID),
            photo = item.data.photo,
            caption = arrayText.joinToString(separator = "\n")
        )
    }
}