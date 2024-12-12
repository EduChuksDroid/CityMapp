package com.educhuks.citymapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.educhuks.citymapp.data.CityResponse
import com.educhuks.citymapp.extensions.divider

@Composable
fun CityList(
    modifier: Modifier,
    cities: List<CityResponse>,
    citySelected: CityResponse?,
    isLandscape: Boolean,
    onItemClick: (CityResponse) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(cities) { item ->
            Text(
                text = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isLandscape && item.id == citySelected?.id) Color.LightGray else Color.Transparent)
                    .divider()
                    .clickable { onItemClick(item) }
                    .padding(16.dp)
            )
        }
    }
}