package com.educhuks.citymapp.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.educhuks.citymapp.domain.CityModel

@Dao
interface CitiesDao {

    @Query("SELECT * FROM favorite_cities")
    suspend fun getAllFavorites(): List<CityModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(city: CityModel)

    @Delete
    suspend fun deleteFavorite(city: CityModel)
}