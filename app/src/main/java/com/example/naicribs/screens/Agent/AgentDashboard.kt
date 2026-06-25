package com.example.naicribs.screens.agent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.naicribs.data.AgentViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun AgentDashboard(navController: NavController, viewModel: AgentViewModel) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadMyListings() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaiCribsColors.Background)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(NaiCribsColors.PurpleDark, NaiCribsColors.Purple)))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Good day,", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                        Text("Agent Dashboard 👋", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                }
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatChip("My listings", "${state.listings.size}")
                    StatChip("Active", "${state.listings.size}")
                }
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Quick Actions", fontSize = 13.sp, color = NaiCribsColors.TextMuted, fontWeight = FontWeight.Medium)

            ActionCard(
                icon = Icons.Default.Add, title = "Add New Listing",
                description = "Post a vacant property",
                bgColor = NaiCribsColors.Purple,
                onClick = { navController.navigate(Routes.AddListing) }
            )
            ActionCard(
                icon = Icons.Default.List, title = "My Listings",
                description = "View, edit or remove your properties",
                bgColor = NaiCribsColors.Surface,
                textColor = NaiCribsColors.PurpleDark,
                iconBgColor = NaiCribsColors.PurpleLight,
                iconTint = NaiCribsColors.Purple,
                onClick = { navController.navigate(Routes.MyListings) }
            )
            ActionCard(
                icon = Icons.Default.Person, title = "My Profile",
                description = "View and update your agent profile",
                bgColor = NaiCribsColors.Surface,
                textColor = NaiCribsColors.PurpleDark,
                iconBgColor = NaiCribsColors.TealLight,
                iconTint = NaiCribsColors.Teal,
                onClick = { navController.navigate(Routes.AgentProfile) }
            )


            Text("More", fontSize = 13.sp, color = NaiCribsColors.TextMuted, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallCard(
                    icon = Icons.Default.Info, label = "About Us",
                    color = NaiCribsColors.PurpleLight, iconColor = NaiCribsColors.Purple,
                    onClick = { navController.navigate(Routes.AboutUs) },
                    modifier = Modifier.weight(1f)
                )
                SmallCard(
                    icon = Icons.Default.HelpOutline, label = "Help",
                    color = NaiCribsColors.TealLight, iconColor = NaiCribsColors.Teal,
                    onClick = { navController.navigate(Routes.Help) },
                    modifier = Modifier.weight(1f)
                )
            }


            if (state.listings.isNotEmpty()) {
                Text("Most Recent Listing", fontSize = 13.sp, color = NaiCribsColors.TextMuted, fontWeight = FontWeight.Medium)
                val latest = state.listings.first()
                Surface(
                    shape = RoundedCornerShape(14.dp), color = NaiCribsColors.Surface,
                    shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(NaiCribsColors.PurpleLight),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.Home, contentDescription = null, tint = NaiCribsColors.Purple) }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(latest.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
                            Text("📍 ${latest.location}", fontSize = 12.sp, color = NaiCribsColors.TextMuted)
                        }
                        Text("KSh ${latest.price.toLong()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.Purple)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Surface(shape = RoundedCornerShape(10.dp), color = Color.White.copy(alpha = 0.15f)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun ActionCard(
    icon: ImageVector, title: String, description: String,
    bgColor: Color, onClick: () -> Unit,
    textColor: Color = Color.White,
    iconBgColor: Color = Color.White.copy(alpha = 0.2f),
    iconTint: Color = Color.White
) {
    Surface(
        onClick = onClick, shape = RoundedCornerShape(14.dp),
        color = bgColor, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(iconBgColor),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp)) }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                Text(description, fontSize = 12.sp, color = textColor.copy(alpha = 0.65f))
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null,
                tint = textColor.copy(alpha = 0.5f), modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun SmallCard(
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