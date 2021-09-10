import java.text.NumberFormat
import java.util.*

data class CarModel(
    val title: String?,
    val price: String?,
    val city: String?,
    val kuzov: String?,
    val obem_dvigatel: String?,
    val probeg: String?,
    val korobka_peredach: String?,
    val rul: String?,
    val color: String?,
    val privod: String?,
    val diff: Double?,
    val photo: String?,
) {
    fun getPriceStr() : String {
        val priceStr = price?.filter { it.isDigit() }
        val priceInt = priceStr?.toInt()

        val myNumber = NumberFormat.getNumberInstance(Locale.US)
            .format(priceInt)
            .replace(",", " ")
        return "$myNumber ₸"
    }
}