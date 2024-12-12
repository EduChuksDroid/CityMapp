package com.educhuks.citymapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educhuks.citymapp.data.CitiesRepository
import com.educhuks.citymapp.data.CityResponse
import kotlinx.coroutines.launch

class MainViewModel(repository: CitiesRepository) : ViewModel() {

    private val _mainEvent = MutableLiveData<MainScreenEvent?>(null)
    val mainEvent: LiveData<MainScreenEvent?> = _mainEvent

    private val _items = MutableLiveData<List<CityResponse>?>(null)
    val items: LiveData<List<CityResponse>?> = _items

    private val _selectedItem = MutableLiveData<CityResponse?>(null)
    val selectedItem: LiveData<CityResponse?> = _selectedItem

    fun selectItem(item: CityResponse) {
        _selectedItem.value = item
    }

    init {
        viewModelScope.launch {
            _mainEvent.value = MainScreenEvent.Loading
            val cities = repository.fetchCities()
            if (cities == null) _mainEvent.value = MainScreenEvent.Error
            else _items.value = cities
            _mainEvent.value = MainScreenEvent.Fetched
        }
    }
}

sealed class MainScreenEvent {
    data object Loading : MainScreenEvent()
    data object Fetched : MainScreenEvent()
    data object Error : MainScreenEvent()
}