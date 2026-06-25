package com.example.naicribs.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.naicribs.data.CustomerViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun CustomerDashboard(navController: NavController, viewModel: CustomerViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaiCribsColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(NaiCribsColors.Teal, NaiCribsColors.TealDark)))
                .padding(24.dp)
        ) {
            Column {
                Text("Welcome 👋", fontSize = 14.sp, color = Color.White.copy(alpha = 0.75f))
                Text("Find Your Crib", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(6.dp))
                Text("${state.listings.size} homes available in Nairobi",
                    fontSize = 13.sp, color = Color.White.copy(alpha = 0.75f))
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            DashboardCard(
                icon = Icons.Default.Search,
                title = "Browse Houses",
                subtitle = "${state.listings.size} listings available",
                color = NaiCribsColors.Teal,
                onClick = { navController.navigate(Routes.Browse) }
            )

            DashboardCard(
                icon = Icons.Default.Bookmark,
                title = "Saved Listings",
                subtitle = if (state.savedListings.isEmpty()) "No saved listings yet"
                else "${state.savedListings.size} saved",
                color = NaiCribsColors.Purple,
                onClick = { navController.navigate(Routes.SavedListings) }
            )

            Text("Browse by area", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
            val areas = listOf("Kitengela" to "🌿", "Ngong" to "🏔️", "Donholm" to "🏙️", "Karen" to "🌳")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                areas.forEach { (area, emoji) ->
                    Surface(
                        onClick = { viewModel.search(area); navController.navigate(Routes.Browse) },
                        shape = RoundedCornerShape(12.dp), color = Color.White,
                        shadowElevation = 1.dp, modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(emoji, fontSize = 22.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(area, fontSize = 11.sp, color = NaiCribsColors.TextPrimary, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Text("Browse by type", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
            listOf("Bedsitter" to "🛏️", "1 Bedroom" to "🛏️", "2 Bedroom" to "🏠", "3 Bedroom" to "🏡").forEach { (type, emoji) ->
                Surface(
                    onClick = { viewModel.filterByType(type); navController.navigate(Routes.Browse) },
                    shape = RoundedCornerShape(12.dp), color = Color.White,
                    shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(emoji, fontSize = 20.sp)
                        Spacer(Modifier.width(12.dp))
                        Text(type, fontSize = 14.sp, color = NaiCribsColors.TextPrimary, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NaiCribsColors.TextMuted)
                    }
                }
            }

            Text("More", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallMenuCard(icon = Icons.Default.Info, label = "About Us",
                    color = NaiCribsColors.PurpleLight, iconColor = NaiCribsColors.Purple,
                    onClick = { navController.navigate(Routes.AboutUs) }, modifier = Modifier.weight(1f))
                SmallMenuCard(icon = Icons.Default.HelpOutline, label = "Help",
                    color = NaiCribsColors.TealLight, iconColor = NaiCribsColors.Teal,
                    onClick = { navController.navigate(Routes.Help) }, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DashboardCard(
    icon: ImageVector, title: String, subtitle: String,
    color: Color, onClick: () -> Unit
) {
    Surface(
        onClick = onClick, shape = RoundedCornerShape(16.dp),
        color = color, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp)) }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text(subtitle, fontSize = 13.sp, color = Color.White.copy(alpha = 0.75f))
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null,
                tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun SmallMenuCard(
    icon: ImageVector, label: String,
    color: Color, iconColor: Color,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick, shape = RoundedCornerShape(14.dp),
        color = Color.White, shadowElevation = 1.dp, modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(color),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp)) }
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NaiCribsColors.TextPrimary)
        }
    }
}