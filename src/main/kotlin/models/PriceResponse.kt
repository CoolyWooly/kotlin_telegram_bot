package models

data class PriceResponse(
    val data: PriceModel?,
) {
    data class PriceModel(
        val name: String?,
        val diffInPercents: Double?,
    )
}
