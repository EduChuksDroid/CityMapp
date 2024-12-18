package com.educhuks.citymapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class CityModel(
    @PrimaryKey val id: Int,
    val country: String,
    val name: String,
    val lon: Double,
    val lat: Double
)