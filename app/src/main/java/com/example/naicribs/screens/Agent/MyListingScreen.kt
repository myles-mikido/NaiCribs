package com.example.naicribs.screens.Agent

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.naicribs.data.AgentViewModel
import com.example.naicribs.models.Apartment
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun MyListingsScreen(navController: NavController, viewModel: AgentViewModel) {
    val state by viewModel.state.collectAsState()
    var deleteTarget by remember { mutableStateOf<Apartment?>(null) }

    LaunchedEffect(Unit) { viewModel.loadMyListings() }

    Scaffold(
        topBar = {
            NaiCribsTopBar(title = "My Listings", onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.AddListing) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.AddListing) },
                containerColor = NaiCribsColors.Purple
            ) { Icon(Icons.Default.Add, contentDescription = "Add listing", tint = Color.White) }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(NaiCribsColors.Background).padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = NaiCribsColors.Purple)
                state.listings.isEmpty() -> EmptyListings(onAdd = { navController.navigate(Routes.AddListing) })
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text("${state.listings.size} active listing${if (state.listings.size != 1) "s" else ""}",
                            fontSize = 13.sp, color = NaiCribsColors.TextMuted)
                    }
                    items(state.listings, key = { it.id }) { apt ->
                        AgentListingCard(
                            apartment = apt,
                            onEdit    = { navController.navigate(Routes.editListing(apt.id)) },
                            onDelete  = { deleteTarget = apt }
                        )
                    }
                }
            }
        }
    }


    deleteTarget?.let { apt ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            icon  = { Icon(Icons.Default.Warning, contentDescription = null, tint = NaiCribsColors.Error) },
            title = { Text("Delete listing?") },
            text  = { Text("\"${apt.name}\" will be permanently removed.") },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteListing(apt.id); deleteTarget = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = NaiCribsColors.Error)) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun AgentListingCard(apartment: Apartment, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                // Thumbnail
                if (apartment.imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = apartment.imageUrls.first(), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp))
                            .background(Brush.linearGradient(listOf(NaiCribsColors.PurpleLight, NaiCribsColors.TealLight))),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Home, contentDescription = null, tint = NaiCribsColors.Purple) }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(apartment.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
                    Text("📍 ${apartment.location}", fontSize = 12.sp, color = NaiCribsColors.TextMuted)
                    Text("KSh ${apartment.price.toLong()} / month", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.Purple)
                }
            }
            HorizontalDivider(color = NaiCribsColors.Border)
            Row {
                TextButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = NaiCribsColors.PurpleDark)
                    Spacer(Modifier.width(4.dp))
                    Text("Edit", color = NaiCribsColors.PurpleDark, fontSize = 13.sp)
                }
                VerticalDivider(modifier = Modifier.height(40.dp), color = NaiCribsColors.Border)
                TextButton(onClick = onDelete, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = NaiCribsColors.Error)
                    Spacer(Modifier.width(4.dp))
                    Text("Delete", color = NaiCribsColors.Error, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun EmptyListings(onAdd: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Home, contentDescription = null, tint = NaiCribsColors.PurpleMid, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(12.dp))
        Text("No listings yet", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
        Text("Tap the button below to post your first property", fontSize = 13.sp, color = NaiCribsColors.TextMuted)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onAdd, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Purple)) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Add listing")
        }
    }
}