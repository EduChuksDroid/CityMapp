package com.educhuks.citymapp.data

import com.google.gson.annotations.SerializedName

data class CityResponse(
    val country: String,
    val name: String,
    @SerializedName("_id" )val id: Int,
    val coord: Coordinates,
    var isFavorite: Boolean = false
)

data class Coordinates(
    val lon: Double,
    val lat: Double
)
