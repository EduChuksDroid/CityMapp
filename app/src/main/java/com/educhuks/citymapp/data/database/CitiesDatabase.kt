package com.educhuks.citymapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.educhuks.citymapp.domain.CityModel

@Database(entities = [CityModel::class], version = 1)
abstract class CitiesDatabase : RoomDatabase() {
    abstract fun citiesDao(): CitiesDao
}