package com.educhuks.citymapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.educhuks.citymapp.data.database.CitiesDao
import com.educhuks.citymapp.data.database.CitiesDatabase
import com.educhuks.citymapp.domain.CityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CitiesDaoTest {

    private lateinit var database: CitiesDatabase
    private lateinit var citiesDao: CitiesDao

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CitiesDatabase::class.java
        ).build()
        citiesDao = database.citiesDao()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        database.close()
    }

    @Test
    fun insertFavorite_and_getAllFavorites() = testScope.runTest {
        val city = CityModel(id = 1, name = "London", country = "UK", lat = 0.0, lon = 0.0)
        citiesDao.insertFavorite(city)

        val favorites = citiesDao.getAllFavorites()
        assertEquals(1, favorites.size)
        assertEquals(city, favorites[0])
    }

    @Test
    fun deleteFavorite() = testScope.runTest {
        val city = CityModel(id = 1, name = "London", country = "UK", lat = 0.0, lon = 0.0)
        citiesDao.insertFavorite(city)
        citiesDao.deleteFavorite(city)

        val favorites = citiesDao.getAllFavorites()
        assertEquals(0, favorites.size)
    }
}