package com.example.naicribs.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

private val NAIROBI_CENTER = LatLng(-1.2921, 36.8219)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(navController: NavController) {   // ✓ no callback needed
    var pickedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var pickedLabel  by remember { mutableStateOf("") }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(NAIROBI_CENTER, 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pin apartment location") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor         = Color(0xFF7F77DD),
                    titleContentColor      = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier             = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment  = Alignment.CenterHorizontally,
                verticalArrangement  = Arrangement.spacedBy(8.dp)
            ) {
                pickedLatLng?.let {
                    Text(
                        "📍 Pinned: %.5f, %.5f".format(it.latitude, it.longitude),
                        fontSize = 13.sp, color = Color(0xFF534AB7)
                    )
                } ?: Text(
                    "Tap anywhere on the map to pin your location",
                    fontSize = 13.sp, color = Color.Gray
                )

                Button(
                    onClick = {
                        pickedLatLng?.let { latlng ->
                            navController.previousBackStackEntry?.savedStateHandle?.apply {
                                set("picked_lat",   latlng.latitude)
                                set("picked_lng",   latlng.longitude)
                                set("picked_label", pickedLabel)
                            }
                            navController.popBackStack()
                        }
                    },
                    enabled  = pickedLatLng != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F77DD))
                ) {
                    Text("Confirm location", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    ) { padding ->
        GoogleMap(
            modifier            = Modifier.fillMaxSize().padding(padding),
            cameraPositionState = cameraPositionState,
            onMapClick          = { latLng ->
                pickedLatLng = latLng
                pickedLabel  = "%.4f, %.4f".format(latLng.latitude, latLng.longitude)
            }
        ) {
            pickedLatLng?.let { latlng ->
                Marker(
                    state   = MarkerState(position = latlng),
                    title   = "Your apartment",
                    snippet = pickedLabel
                )
            }
        }
    }
}