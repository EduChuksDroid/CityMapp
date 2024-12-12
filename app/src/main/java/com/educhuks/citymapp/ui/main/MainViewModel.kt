package com.educhuks.citymapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val items = mockItems(200)

    private val _selectedItem = MutableLiveData<String?>(null)
    val selectedItem: LiveData<String?> = _selectedItem

    fun selectItem(item: String) {
        _selectedItem.value = item
    }

    private fun mockItems(n: Int): List<String> {
        return (1..n).map { "City $it" }
    }
}