package com.educhuks.citymapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.educhuks.citymapp.ui.composables.CityList
import com.educhuks.citymapp.ui.composables.LoadingView
import com.educhuks.citymapp.ui.composables.MapView
import com.educhuks.citymapp.ui.map.MapActivity
import com.educhuks.citymapp.ui.theme.CityMappTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityMappTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val context = LocalContext.current

    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight
        val cities by viewModel.items.observeAsState()
        val citySelected by viewModel.selectedItem.observeAsState()
        val event by viewModel.mainEvent.observeAsState()

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
        }

        Row {
            CityList(
                cities = cities ?: emptyList(),
                modifier = if (isLandscape) Modifier.weight(1f) else Modifier.fillMaxSize(),
                citySelected = citySelected,
                isLandscape = isLandscape,
                onItemClick = { city ->
                    viewModel.selectItem(city)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        LatLng(city.coord.lat, city.coord.lon), 10f
                    )

                    if (!isLandscape) {
                        val mapIntent = Intent(context, MapActivity::class.java).apply {
                            putExtra(MapActivity.NAME_KEY, city.name)
                            putExtra(MapActivity.LAT_KEY, city.coord.lat)
                            putExtra(MapActivity.LON_KEY, city.coord.lon)
                        }
                        context.startActivity(mapIntent)
                    }
                }
            )
            if (isLandscape) MapView(
                city = citySelected?.name.orEmpty(),
                latitude = citySelected?.coord?.lat ?: 0.0,
                longitude = citySelected?.coord?.lon ?: 0.0,
                cameraPositionState = cameraPositionState,
                modifier = Modifier.weight(1f)
            )
        }

        if (event == MainScreenEvent.Loading) LoadingView()
    }
}