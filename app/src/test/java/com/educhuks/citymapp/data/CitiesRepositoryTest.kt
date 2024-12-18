package com.educhuks.citymapp.data

import com.educhuks.citymapp.data.database.CitiesDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesRepositoryTest {

    private lateinit var api: CitiesAPI
    private lateinit var dao: CitiesDao
    private lateinit var repository: CitiesRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        api = mockk(relaxed = true)
        dao = mockk(relaxed = true)
        repository = CitiesRepository(api, dao)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCities returns sorted list of cities`() = testScope.runTest {
        val cities = listOf(
            CityResponse("UK", "London", 1, Coordinates(0.0, 0.0)),
            CityResponse("FR", "Paris", 2, Coordinates(0.0, 0.0)),
            CityResponse("US", "New York", 3, Coordinates(0.0, 0.0))
        )
        coEvery { api.getCities() } returns cities
        coEvery { dao.getAllFavorites() } returns emptyList()

        val result = repository.fetchCities()
        val orderedCities = cities.sortedWith(compareBy({ it.name }, { it.country }, { it.coord.lat }, { it.coord.lon }))

        assertEquals(orderedCities, result)
    }

    @Test
    fun `fetchCities returns null on error`() = testScope.runTest {
        coEvery { api.getCities() } throws HttpException(mockk(relaxed = true))

        val result = repository.fetchCities()

        assertEquals(null, result)
    }
}