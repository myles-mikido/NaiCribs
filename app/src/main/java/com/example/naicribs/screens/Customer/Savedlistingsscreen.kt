package com.example.naicribs.screens.customer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.naicribs.data.CustomerViewModel
import com.example.naicribs.models.Apartment
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun SavedListingsScreen(navController: NavController, viewModel: CustomerViewModel) {
    val state   by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { NaiCribsTopBar(title = "Saved Listings 🔖", onBackClick = { navController.popBackStack() }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NaiCribsColors.Background)
                .padding(padding)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = NaiCribsColors.Purple
                )
                state.savedListings.isEmpty() -> EmptySaved(
                    onBrowse = { navController.navigate(Routes.Browse) }
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "${state.savedListings.size} saved listing${if (state.savedListings.size != 1) "s" else ""}",
                            fontSize = 13.sp, color = NaiCribsColors.TextMuted
                        )
                    }
                    items(state.savedListings, key = { it.id }) { apt ->
                        SavedListingCard(
                            apartment = apt,
                            onCall  = { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${apt.contactNumber}"))) },
                            onEmail = { context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${apt.email}"))) },
                            onMap   = {
                                if (apt.latitude != null && apt.longitude != null) {
                                    navController.navigate(Routes.mapViewer(apt.name, apt.latitude, apt.longitude, apt.location))
                                }
                            },
                            onRemove = { viewModel.toggleSave(apt.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedListingCard(
    apartment: Apartment,
    onCall: () -> Unit,
    onEmail: () -> Unit,
    onMap: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box {
                if (apartment.imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = apartment.imageUrls.first(), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(Brush.linearGradient(listOf(NaiCribsColors.PurpleLight, NaiCribsColors.TealLight))),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Home, contentDescription = null, tint = NaiCribsColors.Purple, modifier = Modifier.size(40.dp)) }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp), color = NaiCribsColors.Purple,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
                ) {
                    Text("KSh ${apartment.price.toLong()}/mo",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.align(Alignment.TopEnd).padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                ) {
                    Icon(Icons.Default.BookmarkRemove, contentDescription = "Remove saved",
                        tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(apartment.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NaiCribsColors.TextPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = NaiCribsColors.TextMuted, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text(apartment.location, fontSize = 12.sp, color = NaiCribsColors.TextMuted)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = NaiCribsColors.TextMuted, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text(apartment.houseType, fontSize = 12.sp, color = NaiCribsColors.TextMuted)
                    }
                }

                if (apartment.tags.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        apartment.tags.take(3).forEach { tag ->
                            Surface(shape = RoundedCornerShape(20.dp), color = NaiCribsColors.PurpleLight) {
                                Text(tag, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontSize = 11.sp, color = NaiCribsColors.PurpleDark)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionButton(Icons.Default.Phone, "Call", NaiCribsColors.TealLight, NaiCribsColors.TealDark, onCall, Modifier.weight(1f))
                    ActionButton(Icons.Default.Email, "Email", NaiCribsColors.PurpleLight, NaiCribsColors.PurpleDark, onEmail, Modifier.weight(1f))
                    if (apartment.latitude != null && apartment.longitude != null) {
                        ActionButton(Icons.Default.Map, "Map", Color(0xFFFFF3E0), Color(0xFFE65100), onMap, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector, text: String,
    bgColor: Color, textColor: Color, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(onClick = onClick, shape = RoundedCornerShape(10.dp), color = bgColor, modifier = modifier) {
        Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textColor)
        }
    }
}

@Composable
private fun EmptySaved(onBrowse: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🔖", fontSize = 52.sp)
        Spacer(Modifier.height(12.dp))
        Text("No saved listings yet", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
        Text("Tap the bookmark icon on any listing to save it here", fontSize = 13.sp, color = NaiCribsColors.TextMuted)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onBrowse, shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Purple)) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Browse listings")
        }
    }
}