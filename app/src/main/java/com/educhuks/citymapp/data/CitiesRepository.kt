package com.educhuks.citymapp.data

class CitiesRepository(private val citiesAPI: CitiesAPI) {

    // I added lat and lon to the sorting criteria because the list contains duplicate cities with the same countries
    suspend fun fetchCities(): List<CityResponse>? {
        return try {
            citiesAPI.getCities().sortedWith(
                compareBy({ it.name }, { it.country }, { it.coord.lat}, {it.coord.lon})
            )
        } catch (e: Exception) {
            null
        }
    }
}