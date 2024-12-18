package com.educhuks.citymapp.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.educhuks.citymapp.data.CityResponse
import com.educhuks.citymapp.ui.composables.NavRoutes.DETAIL
import com.educhuks.citymapp.ui.composables.NavRoutes.LIST
import com.educhuks.citymapp.ui.main.CitiesViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun CitiesScreen(
    modifier: Modifier,
    viewModel: CitiesViewModel,
    onItemClick: (CityResponse) -> Unit
) {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(key1 = configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            && navController.currentDestination?.route == DETAIL) {
            navController.navigateUp()
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = LIST
    ) {
        composable(LIST) {
            CityList (
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            ) { item ->
                onItemClick(item)
                if (!isLandscape) navController.navigate(DETAIL)
            }
        }
        composable(DETAIL) {
            val latitude = viewModel.selectedItem.value?.coord?.lat ?: 0.0
            val longitude = viewModel.selectedItem.value?.coord?.lon ?: 0.0
            MapView(
                viewModel = viewModel,
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 10f)
                },
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}