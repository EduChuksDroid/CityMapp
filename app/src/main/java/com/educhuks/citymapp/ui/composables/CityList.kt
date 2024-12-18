package com.educhuks.citymapp.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.educhuks.citymapp.R
import com.educhuks.citymapp.data.CityResponse
import com.educhuks.citymapp.extensions.divider
import com.educhuks.citymapp.ui.main.CitiesViewModel
import com.educhuks.citymapp.ui.theme.subtitleStyle
import com.educhuks.citymapp.ui.theme.titleStyle

@Composable
fun CityList(
    modifier: Modifier,
    viewModel: CitiesViewModel,
    onItemClick: (CityResponse) -> Unit
) {
    val searchText by viewModel.queryPrefix.collectAsState()
    val cities by viewModel.filteredItems.collectAsState()
    val citySelected by viewModel.selectedItem.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val displayFavorites by viewModel.displayFavorites.collectAsState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { viewModel.updateQueryPrefix(it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                placeholder = { Text(stringResource(R.string.hint_search_city)) },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { viewModel.toggleFavoritesList() }) {
                Icon(
                    imageVector = if (displayFavorites) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite,
                    contentDescription = null
                )
            }
        }
        LazyColumn {
            items(cities) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isLandscape && item.id == citySelected?.id) Color.LightGray else Color.Transparent)
                        .divider()
                        .clickable { onItemClick(item) }
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${item.name}, ${item.country}",
                            style = titleStyle
                        )
                        Text(
                            text = "${item.coord.lat}, ${item.coord.lon}",
                            style = subtitleStyle
                        )
                    }
                    IconButton(onClick = { viewModel.toggleFavorite(item) }) {
                        Icon(
                            imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}