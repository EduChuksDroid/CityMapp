package com.educhuks.citymapp.ui.map

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.educhuks.citymapp.ui.composables.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

class MapActivity: ComponentActivity() {

    private val latitude: Double by lazy { intent.getDoubleExtra(LAT_KEY, 0.0) }
    private val longitude: Double by lazy { intent.getDoubleExtra(LON_KEY, 0.0) }
    private val name: String by lazy { intent.getStringExtra(NAME_KEY) ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapView(
                city = name,
                latitude = latitude,
                longitude = longitude,
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 10f)
                }
            )
        }
    }

    @SuppressLint("ChromeOsOnConfigurationChanged")
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish()
        }
    }

    companion object {
        const val NAME_KEY = "NAME"
        const val LAT_KEY = "latitude"
        const val LON_KEY = "longitude"
    }
}