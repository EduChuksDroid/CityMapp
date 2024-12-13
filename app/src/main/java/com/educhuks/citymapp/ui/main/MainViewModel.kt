package com.educhuks.citymapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educhuks.citymapp.data.CitiesRepository
import com.educhuks.citymapp.data.CityResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(repository: CitiesRepository) : ViewModel() {

    private var fullList: List<CityResponse> = listOf()

    private val _mainEvent = MutableLiveData<MainScreenEvent?>(null)
    val mainEvent: LiveData<MainScreenEvent?> = _mainEvent

    private val _filteredItems = MutableStateFlow(fullList)
    val filteredItems: StateFlow<List<CityResponse>> = _filteredItems.asStateFlow()

    private val _queryPrefix = MutableStateFlow("")
    val queryPrefix: StateFlow<String> = _queryPrefix.asStateFlow()

    private val _selectedItem = MutableStateFlow<CityResponse?>(null)
    val selectedItem: StateFlow<CityResponse?> = _selectedItem.asStateFlow()

    fun selectItem(item: CityResponse) {
        _selectedItem.value = item
    }

    fun updateQueryPrefix(prefix: String) {
        _queryPrefix.value = prefix
        _selectedItem.value = null
        _filteredItems.value = filterCitiesByPrefix(fullList, prefix)
    }

    private fun filterCitiesByPrefix(cities: List<CityResponse>, prefix: String): List<CityResponse> {
        if (prefix.isEmpty()) return cities

        val filteredCities = mutableListOf<CityResponse>()
        for (city in cities) {
            if (city.name.startsWith(prefix)) {
                filteredCities.add(city)
            }
        }
        return filteredCities
    }

    init {
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
}

sealed class MainScreenEvent {
    data object Loading : MainScreenEvent()
    data object Fetched : MainScreenEvent()
    data object Error : MainScreenEvent()
}