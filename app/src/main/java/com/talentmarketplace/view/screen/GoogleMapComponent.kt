package com.talentmarketplace.view.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GoogleMapComponent(
    modifier: Modifier = Modifier,
    onLocationSelected: (LatLng) -> Unit,
    jobLocation: State<LatLng?>,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            jobLocation.value ?: LatLng(53.2740, -9.0513), 10f)
    }

    GoogleMap (
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            onLocationSelected(latLng)
                     },
    ) {
        jobLocation.value?.let { location ->
            Marker(
                state = MarkerState(position = location),
                title = "Selected location",
            )
        }
    }
}