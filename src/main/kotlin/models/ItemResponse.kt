package models

import models.CarModel

data class ItemResponse(
    val success: Boolean,
    val data: CarModel?,
)
