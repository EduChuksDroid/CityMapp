package com.educhuks.citymapp.data

import com.educhuks.citymapp.data.database.CitiesDao
import com.educhuks.citymapp.domain.CityModel

class CitiesRepository(
    private val citiesAPI: CitiesAPI,
    private val citiesDao: CitiesDao
) {

    // I added lat and lon to the sorting criteria because the list contains duplicate cities with the same countries
    suspend fun fetchCities(): List<CityResponse>? {
        return try {
            val cities = citiesAPI.getCities().sortedWith(
                compareBy({ it.name }, { it.country }, { it.coord.lat}, {it.coord.lon})
            )
            val favoriteCities = citiesDao.getAllFavorites().map { it.id }.toSet()
            cities.map { it.copy(isFavorite = it.id in favoriteCities) }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveFavorite(city: CityResponse) {
        citiesDao.insertFavorite(
            CityModel(city.id, city.country, city.name, city.coord.lon, city.coord.lat)
        )
    }

    suspend fun removeFavorite(city: CityResponse) {
        citiesDao.deleteFavorite(
            CityModel(city.id, city.country, city.name, city.coord.lon, city.coord.lat)
        )
    }
}