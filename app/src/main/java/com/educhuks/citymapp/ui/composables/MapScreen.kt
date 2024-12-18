package com.educhuks.citymapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.educhuks.citymapp.R
import com.educhuks.citymapp.ui.main.CitiesViewModel
import com.google.maps.android.compose.CameraPositionState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: CitiesViewModel,
    cameraPositionState: CameraPositionState
) {
    val city by viewModel.selectedItem.collectAsState()

    Box(modifier = modifier) {
        if (city != null) {
            MapView(
                viewModel = viewModel,
                cameraPositionState = cameraPositionState
            )
        } else Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.label_no_city_selected))
        }
    }
}