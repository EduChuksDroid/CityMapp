package com.educhuks.citymapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateViewModelFactory
import com.educhuks.citymapp.ui.composables.CityList
import com.educhuks.citymapp.ui.composables.MapView
import com.educhuks.citymapp.ui.map.MapActivity
import com.educhuks.citymapp.ui.theme.CityMappTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels { SavedStateViewModelFactory(application, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityMappTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight
        val citySelected by viewModel.selectedItem.observeAsState()
        Row {
            CityList(
                cities = viewModel.items,
                modifier = if (isLandscape) Modifier.weight(1f) else Modifier.fillMaxSize(),
                citySelected = citySelected,
                isLandscape = isLandscape,
                onItemClick = { city ->
                    viewModel.selectItem(city)

                    if (!isLandscape) {
                        val mapIntent = Intent(context, MapActivity::class.java).apply {
                            putExtra(MapActivity.NAME_KEY, city)
                        }
                        context.startActivity(mapIntent)
                    }
                }
            )
            if (isLandscape) MapView(
                item = viewModel.selectedItem.value ?: "",
                modifier = Modifier.weight(1f)
            )
        }
    }
}