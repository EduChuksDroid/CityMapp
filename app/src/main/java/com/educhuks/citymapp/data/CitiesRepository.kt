package com.educhuks.citymapp.data

class CitiesRepository(private val citiesAPI: CitiesAPI) {

    suspend fun fetchCities(): List<CityResponse>? {
        return try {
            citiesAPI.getCities().sortedBy { it.name }
        } catch (e: Exception) {
            null
        }
    }
}