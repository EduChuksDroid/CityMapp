package com.educhuks.citymapp.ui

import com.educhuks.citymapp.data.CitiesRepository
import com.educhuks.citymapp.data.CityResponse
import com.educhuks.citymapp.data.Coordinates
import com.educhuks.citymapp.ui.main.CitiesViewModel
import com.educhuks.citymapp.util.InstantExecutorExtension
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
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
@ExperimentalCoroutinesApi
class CitiesViewModelTest {

    private lateinit var viewModel: CitiesViewModel
    private lateinit var repository: CitiesRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun before(){
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = CitiesViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateQueryPrefix should update queryPrefix, clear selectedItem and filter cities`() = testScope.runTest {
        val cities = listOf(
            CityResponse("UK", "London", 1, Coordinates(0.0, 0.0)),
            CityResponse("FR", "Paris", 2, Coordinates(0.0, 0.0)),
            CityResponse("US", "New York", 3, Coordinates(0.0, 0.0))
        )

        coEvery { repository.fetchCities() } returns cities

        viewModel.fetchCities()

        viewModel.updateQueryPrefix("Lo")

        assertEquals("Lo", viewModel.queryPrefix.value)
        assertEquals(null, viewModel.selectedItem.value)
        assertEquals(listOf(cities[0]), viewModel.filteredItems.value)
    }

    @Test
    fun `updateQueryPrefix with empty prefix should show all cities`() = testScope.runTest {
        val cities = listOf(
            CityResponse("UK", "London", 1, Coordinates(0.0, 0.0)),
            CityResponse("FR", "Paris", 2, Coordinates(0.0, 0.0)),
            CityResponse("US", "New York", 3, Coordinates(0.0, 0.0))
        )

        coEvery { repository.fetchCities() } returns cities

        viewModel.fetchCities()

        viewModel.updateQueryPrefix("")

        assertEquals("", viewModel.queryPrefix.value)
        assertEquals(null, viewModel.selectedItem.value)
        assertEquals(cities, viewModel.filteredItems.value)
    }

    @Test
    fun `updateQueryPrefix with invalid prefix should show no cities`() = testScope.runTest {
        val cities = listOf(
            CityResponse("UK", "London", 1, Coordinates(0.0, 0.0)),
            CityResponse("FR", "Paris", 2, Coordinates(0.0, 0.0)),
            CityResponse("US", "New York", 3, Coordinates(0.0, 0.0))
        )

        coEvery { repository.fetchCities() } returns cities

        viewModel.fetchCities()

        viewModel.updateQueryPrefix("xyz")

        assertEquals("xyz", viewModel.queryPrefix.value)
        assertEquals(null, viewModel.selectedItem.value)
        assertEquals(emptyList<CityResponse>(), viewModel.filteredItems.value)
    }

    @Test
    fun `updateQueryPrefix with mixed case prefix should filter correctly`() = testScope.runTest {
        val cities = listOf(
            CityResponse("UK", "London", 1, Coordinates(0.0, 0.0)),
            CityResponse("FR", "Paris", 2, Coordinates(0.0, 0.0)),
            CityResponse("US", "New York", 3, Coordinates(0.0, 0.0))
        )

        coEvery { repository.fetchCities() } returns cities

        viewModel.fetchCities()

        viewModel.updateQueryPrefix("lOn")

        assertEquals("lOn", viewModel.queryPrefix.value)
        assertEquals(null, viewModel.selectedItem.value)
        assertEquals(emptyList<CityResponse>(), viewModel.filteredItems.value)
    }

    @Test
    fun `fetchCities should update fullList and filteredItems`() = testScope.runTest {
        val cities = listOf(
            CityResponse("UK", "London", 1, Coordinates(0.0, 0.0)),
            CityResponse("FR", "Paris", 2, Coordinates(0.0, 0.0)),
            CityResponse("US", "New York", 3, Coordinates(0.0, 0.0))
        )

        coEvery { repository.fetchCities() } returns cities

        viewModel.fetchCities()

        assertEquals(cities, viewModel.filteredItems.value)
    }

    @Test
    fun `fetchCities should handle error`() = testScope.runTest {
        coEvery { repository.fetchCities() } returns null

        viewModel.fetchCities()

        assertEquals(emptyList<CityResponse>(), viewModel.filteredItems.value)
    }

    @Test
    fun `selectItem should update selectedItem`() = testScope.runTest {
        val city = CityResponse("UK", "London", 1, Coordinates(0.0, 0.0))

        viewModel.selectItem(city)

        assertEquals(city, viewModel.selectedItem.value)
    }
}