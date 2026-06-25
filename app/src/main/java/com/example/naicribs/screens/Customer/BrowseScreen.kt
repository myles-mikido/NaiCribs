package com.example.naicribs.screens.Customer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.naicribs.screens.common.ImageLightbox
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun BrowseScreen(navController: NavController, viewModel: CustomerViewModel) {
    val state   by viewModel.state.collectAsState()
    val context = LocalContext.current

    var lightboxImages by remember { mutableStateOf<List<String>>(emptyList()) }
    var lightboxIndex  by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = { NaiCribsTopBar(title = "Find Your Crib 🏠") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NaiCribsColors.Background)
                .padding(padding)
        ) {
            OutlinedTextField(
                value         = state.searchQuery,
                onValueChange = { viewModel.search(it) },
                placeholder   = { Text("Search location or apartment...", fontSize = 13.sp) },
                leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null, tint = NaiCribsColors.TextMuted) },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                shape         = RoundedCornerShape(14.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = NaiCribsColors.Purple,
                    unfocusedBorderColor    = NaiCribsColors.Border,
                    focusedLabelColor       = NaiCribsColors.Purple,
                    cursorColor             = NaiCribsColors.Purple,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor   = Color.White
                )
            )

            LazyRow(
                contentPadding        = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.filterOptions) { filter ->
                    FilterChip(
                        selected = state.activeFilter == filter,
                        onClick  = { viewModel.filterByType(filter) },
                        label    = { Text(filter, fontSize = 12.sp) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NaiCribsColors.Purple,
                            selectedLabelColor     = Color.White
                        )
                    )
                }
            }
            Spacer(Modifier.height(6.dp))

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NaiCribsColors.Purple)
                }
                state.filteredListings.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("😕", fontSize = 40.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("No listings found", fontSize = 16.sp, color = NaiCribsColors.TextMuted)
                    }
                }
                else -> LazyColumn(
                    contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement   = Arrangement.spacedBy(14.dp)
                ) {
                    items(state.filteredListings, key = { it.id }) { apt ->
                        CustomerListingCard(
                            apartment    = apt,
                            onCall       = { context.startActivity(Intent(Intent.ACTION_DIAL,    Uri.parse("tel:${apt.contactNumber}"))) },
                            onEmail      = { context.startActivity(Intent(Intent.ACTION_SENDTO,  Uri.parse("mailto:${apt.email}"))) },
                            onMap        = {
                                if (apt.latitude != null && apt.longitude != null) {
                                    navController.navigate(Routes.mapViewer(apt.name, apt.latitude, apt.longitude, apt.location))
                                }
                            },
                            onImageClick = { index ->
                                if (apt.imageUrls.isNotEmpty()) {
                                    lightboxImages = apt.imageUrls
                                    lightboxIndex  = index
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    lightboxIndex?.let { idx ->
        ImageLightbox(
            imageUrls    = lightboxImages,
            initialIndex = idx,
            onDismiss    = { lightboxIndex = null }
        )
    }
}

@Composable
private fun CustomerListingCard(
    apartment: Apartment,
    onCall: () -> Unit,
    onEmail: () -> Unit,
    onMap: () -> Unit,
    onImageClick: (Int) -> Unit
) {
    Surface(
        shape           = RoundedCornerShape(18.dp),
        color           = Color.White,
        shadowElevation = 3.dp,
        modifier        = Modifier.fillMaxWidth()
    ) {
        Column {
            if (apartment.imageUrls.isNotEmpty()) {
                val pagerState = rememberPagerState(pageCount = { apartment.imageUrls.size })
                Box {
                    HorizontalPager(state = pagerState) { page ->
                        AsyncImage(
                            model              = apartment.imageUrls[page],
                            contentDescription = null,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                                .clickable { onImageClick(page) }
                        )
                    }

                    Surface(
                        shape    = RoundedCornerShape(8.dp),
                        color    = NaiCribsColors.Purple,
                        modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
                    ) {
                        Text(
                            "KSh ${apartment.price.toLong()}/mo",
                            modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize   = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White
                        )
                    }

                    Surface(
                        shape    = RoundedCornerShape(6.dp),
                        color    = Color.Black.copy(alpha = 0.45f),
                        modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                    ) {
                        Text("🔍 Tap to zoom", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontSize = 10.sp, color = Color.White)
                    }

                    if (apartment.imageUrls.size > 1) {
                        Row(
                            modifier              = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(apartment.imageUrls.size) { i ->
                                val w = if (i == pagerState.currentPage) 16.dp else 6.dp
                                Box(
                                    modifier = Modifier
                                        .width(w).height(6.dp)
                                        .background(
                                            if (i == pagerState.currentPage) Color.White else Color.White.copy(alpha = 0.5f),
                                            RoundedCornerShape(3.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(140.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(Brush.linearGradient(listOf(NaiCribsColors.PurpleLight, NaiCribsColors.TealLight))),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = NaiCribsColors.Purple, modifier = Modifier.size(40.dp))
                        Text("No photos yet", fontSize = 12.sp, color = NaiCribsColors.TextMuted)
                    }
                    Surface(
                        shape    = RoundedCornerShape(8.dp), color = NaiCribsColors.Purple,
                        modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
                    ) {
                        Text("KSh ${apartment.price.toLong()}/mo", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(apartment.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NaiCribsColors.TextPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ListingMetaItem(icon = Icons.Default.LocationOn, text = apartment.location)
                    ListingMetaItem(icon = Icons.Default.Home, text = apartment.houseType)
                }
                if (apartment.description.isNotBlank()) {
                    Text(apartment.description, fontSize = 13.sp, color = NaiCribsColors.TextMuted, maxLines = 2)
                }
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ListingActionBtn(icon = Icons.Default.Phone, text = "Call",  bgColor = NaiCribsColors.TealLight,   textColor = NaiCribsColors.TealDark,   onClick = onCall,  modifier = Modifier.weight(1f))
                    ListingActionBtn(icon = Icons.Default.Email, text = "Email", bgColor = NaiCribsColors.PurpleLight, textColor = NaiCribsColors.PurpleDark, onClick = onEmail, modifier = Modifier.weight(1f))
                    if (apartment.latitude != null && apartment.longitude != null) {
                        ListingActionBtn(icon = Icons.Default.Map, text = "Map", bgColor = Color(0xFFFFF3E0), textColor = Color(0xFFE65100), onClick = onMap, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ListingMetaItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = NaiCribsColors.TextMuted, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(3.dp))
        Text(text, fontSize = 12.sp, color = NaiCribsColors.TextMuted)
    }
}

@Composable
private fun ListingActionBtn(
    icon: ImageVector, text: String,
    bgColor: Color, textColor: Color,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(onClick = onClick, shape = RoundedCornerShape(10.dp), color = bgColor, modifier = modifier) {
        Row(
            modifier              = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textColor)
        }
    }
}