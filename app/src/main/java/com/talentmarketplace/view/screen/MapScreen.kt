package com.talentmarketplace.view.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.talentmarketplace.view.navigation.LocalNavController
import com.talentmarketplace.viewmodel.JobPostViewModel

@Composable
fun MapScreen(
    jobPostID: String?,
    jobPostViewModel: JobPostViewModel = hiltViewModel(),
    navController: NavController = LocalNavController.current,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(53.2740, -9.0513), 10f)
    }
    // var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var markerPosition by jobPostViewModel.jobLocation

    LaunchedEffect(jobPostViewModel) {
        jobPostViewModel.navEvent.collect {
                route -> navController.navigate(route)
        }
    }

    GoogleMap (
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {
            latLng -> markerPosition = latLng
        }
    ) {
        markerPosition?.let {latLng ->
            Marker(
                state = MarkerState(position = latLng),
                title = "Selected location",
            )
        }
    }

    Button (
        onClick = {
            markerPosition?.let { latLng ->
                jobPostViewModel.updateLocation(latLng)
                // jobPostViewModel.onConfirmLocation()
                navController.popBackStack()
            }
        },
        enabled = markerPosition != null
    ) {
        Text("Confirm Location")
    }
}