package com.educhuks.citymapp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.educhuks.citymapp.ui.composables.CitiesScreen
import com.educhuks.citymapp.ui.composables.LoadingView
import com.educhuks.citymapp.ui.composables.MapScreen
import com.educhuks.citymapp.ui.theme.CityMappTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

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
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val event by viewModel.mainEvent.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }

    Box {
        Row {
            CitiesScreen(
                modifier = if (isLandscape) Modifier.weight(1f) else Modifier.fillMaxSize(),
                viewModel = viewModel,
                onItemClick = { city ->
                    viewModel.selectItem(city)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        LatLng(city.coord.lat, city.coord.lon), 10f
                    )
                }
            )
            if (isLandscape) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.Gray)
                )
                MapScreen(
                    modifier = Modifier.weight(1f),
                    viewModel = viewModel,
                    cameraPositionState = cameraPositionState
                )
            }
        }

        if (event == MainScreenEvent.Loading) LoadingView()
        // TODO error event handling
    }
}