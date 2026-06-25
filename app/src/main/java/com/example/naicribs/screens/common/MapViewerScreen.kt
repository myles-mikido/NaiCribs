package com.example.naicribs.screens.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewerScreen(
    navController: NavController,
    apartmentName: String,
    latitude: Double,
    longitude: Double,
    locationLabel: String
) {
    val context = LocalContext.current
    val apartmentLatLng = LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(apartmentLatLng, 16f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(apartmentName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF7F77DD),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📍 $locationLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF534AB7)
                )

                Button(
                    onClick = {
                        val geoUri = Uri.parse(
                            "geo:$latitude,$longitude?q=$latitude,$longitude($apartmentName)"
                        )
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        // Fallback to browser if Maps app not installed
                        val intent = if (mapIntent.resolveActivity(context.packageManager) != null) {
                            mapIntent
                        } else {
                            Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://maps.google.com/?q=$latitude,$longitude"))
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D9E75))
                ) {
                    Text(
                        text = "Open in Google Maps",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            Marker(
                state = MarkerState(position = apartmentLatLng),
                title = apartmentName,
                snippet = locationLabel
            )
        }
    }
}