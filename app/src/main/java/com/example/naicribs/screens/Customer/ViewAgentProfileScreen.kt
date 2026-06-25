package com.example.naicribs.screens.Customer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.naicribs.data.ListingRepository
import com.example.naicribs.models.Apartment
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors
import kotlinx.coroutines.launch

@Composable
fun ViewAgentProfileScreen(navController: NavController, agentId: String) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val repo    = remember { ListingRepository() }

    var listings  by remember { mutableStateOf<List<Apartment>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(agentId) {
        scope.launch {
            val result = repo.getAgentListings(agentId)
            listings  = result.getOrDefault(emptyList())
            isLoading = false
        }
    }

    Scaffold(
        topBar = { NaiCribsTopBar(title = "Agent Profile", onBackClick = { navController.popBackStack() }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(NaiCribsColors.Background).padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                        .background(Brush.verticalGradient(listOf(NaiCribsColors.PurpleDark, NaiCribsColors.Purple))),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(68.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp)) }
                        Spacer(Modifier.height(8.dp))
                        Text("Agent", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("${listings.size} listing${if (listings.size != 1) "s" else ""}", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
            item { Text("Listings by this agent", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary, modifier = Modifier.padding(horizontal = 16.dp)) }
            item { Spacer(Modifier.height(8.dp)) }

            if (isLoading) {
                item { Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = NaiCribsColors.Purple) } }
            } else {
                items(listings, key = { it.id }) { apt ->
                    Surface(
                        shape = RoundedCornerShape(14.dp), color = Color.White,
                        shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (apt.imageUrls.isNotEmpty()) {
                                AsyncImage(model = apt.imageUrls.first(), contentDescription = null, contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(10.dp)))
                            } else {
                                Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(10.dp)).background(NaiCribsColors.PurpleLight),
                                    contentAlignment = Alignment.Center) { Icon(Icons.Default.Home, contentDescription = null, tint = NaiCribsColors.Purple) }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(apt.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
                                Text("📍 ${apt.location}", fontSize = 12.sp, color = NaiCribsColors.TextMuted)
                                Text("KSh ${apt.price.toLong()}/mo", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NaiCribsColors.Purple)
                            }
                            IconButton(onClick = { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${apt.contactNumber}"))) }) {
                                Icon(Icons.Default.Phone, contentDescription = "Call", tint = NaiCribsColors.Teal)
                            }
                        }
                    }
                }
            }
        }
    }
}