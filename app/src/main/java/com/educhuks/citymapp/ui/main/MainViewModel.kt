package com.educhuks.citymapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educhuks.citymapp.data.CitiesRepository
import com.educhuks.citymapp.data.CityResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CitiesRepository) : ViewModel() {

    private var fullList: List<CityResponse> = listOf()

    private val _mainEvent = MutableStateFlow<MainScreenEvent?>(null)
    val mainEvent: StateFlow<MainScreenEvent?> = _mainEvent.asStateFlow()

    private val _filteredItems = MutableStateFlow(fullList)
    val filteredItems: StateFlow<List<CityResponse>> = _filteredItems.asStateFlow()

    private val _queryPrefix = MutableStateFlow("")
    val queryPrefix: StateFlow<String> = _queryPrefix.asStateFlow()

    private val _selectedItem = MutableStateFlow<CityResponse?>(null)
    val selectedItem: StateFlow<CityResponse?> = _selectedItem.asStateFlow()

    private val _displayFavorites = MutableStateFlow(true)
    val displayFavorites: StateFlow<Boolean> = _displayFavorites.asStateFlow()

    fun selectItem(item: CityResponse) {
        _selectedItem.value = item
    }

    fun updateQueryPrefix(prefix: String) {
        _queryPrefix.value = prefix
        _selectedItem.value = null
        _filteredItems.value = filterCitiesByPrefix(prefix)
    }

    fun toggleFavoritesList() {
        _displayFavorites.value = !_displayFavorites.value
        updateQueryPrefix("")
    }

    fun fetchCities() {
        viewModelScope.launch {
            _mainEvent.value = MainScreenEvent.Loading
            val cities = repository.fetchCities()
            if (cities == null) _mainEvent.value = MainScreenEvent.Error
            else {
                fullList = cities
                _filteredItems.value = cities
            }
            _mainEvent.value = MainScreenEvent.Fetched
        }
    }

    fun toggleFavorite(city: CityResponse) {
        viewModelScope.launch {
            if (city.isFavorite) {
                repository.removeFavorite(city)
            } else {
                repository.saveFavorite(city)
            }
            fullList = fullList.map {
                if (it.id == city.id) it.copy(isFavorite = !it.isFavorite) else it
            }
            _filteredItems.value = filterCitiesByPrefix(_queryPrefix.value)
        }
    }

    private fun filterCitiesByPrefix(prefix: String): List<CityResponse> {
        val tempCities =
            if (_displayFavorites.value) fullList.toList()
            else fullList.filter { it.isFavorite }

        if (prefix.isEmpty()) return tempCities

        val filteredCities = mutableListOf<CityResponse>()

        tempCities.forEach { city ->
            if (city.name.startsWith(prefix)) {
                filteredCities.add(city)
            }
        }
        return filteredCities
    }

    init { fetchCities() }
}

sealed class MainScreenEvent {
    data object Loading : MainScreenEvent()
    data object Fetched : MainScreenEvent()
    data object Error : MainScreenEvent()
}