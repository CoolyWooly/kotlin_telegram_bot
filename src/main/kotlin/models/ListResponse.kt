package models

import models.CarItemModel

data class ListResponse(
    val success: Boolean,
    val diff: Int,
    val data: List<CarItemModel>?,
)
