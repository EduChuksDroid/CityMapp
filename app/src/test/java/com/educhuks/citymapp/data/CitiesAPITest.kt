package com.educhuks.citymapp.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesAPITest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: CitiesAPI
    private lateinit var gson: Gson

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockWebServer = MockWebServer()
        gson = GsonBuilder().create()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CitiesAPI::class.java)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchCities returns list of cities`() = testScope.runTest {
        val mockResponse = MockResponse()
            .setBody("""[{"name":"London","country":"UK","id":1,"coord":{"lat":0.0,"lon":0.0}},{"name":"Paris","country":"FR","id":2,"coord":{"lat":0.0,"lon":0.0}}]""")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val cities = api.getCities()

        assertEquals(2, cities.size)
        assertEquals("London", cities[0].name)
        assertEquals("Paris", cities[1].name)
    }

    @Test
    fun `fetchCities throws exception on error`() = testScope.runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        assertThrows<HttpException> { api.getCities() }
    }
}