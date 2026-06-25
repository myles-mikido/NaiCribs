package com.example.naicribs.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun AboutUsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            NaiCribsTopBar(title = "About NaiCribs", onBackClick = { navController.popBackStack() })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NaiCribsColors.Background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(listOf(NaiCribsColors.PurpleDark, NaiCribsColors.Purple))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏠", fontSize = 52.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("NaiCribs", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Find your perfect crib in Nairobi", fontSize = 13.sp, color = Color.White.copy(alpha = 0.75f))
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AboutSection(
                    title = "Who We Are",
                    body  = "NaiCribs is Nairobi's #1 house hunting platform, connecting property seekers with trusted agents across the city. We make it easy to find your dream home — whether it's a bedsitter in Kitengela or a 3-bedroom in Karen."
                )
                AboutSection(
                    title = "Our Mission",
                    body  = "To simplify the house-hunting process in Nairobi by creating a transparent, fast, and mobile-first platform that works for both tenants and agents."
                )
                AboutSection(
                    title = "For Agents",
                    body  = "List your available properties in minutes. Upload photos, pin your location on a map, add area tags, and connect directly with interested customers."
                )
                AboutSection(
                    title = "For Customers",
                    body  = "Browse hundreds of listings, filter by area and house type, save your favourites, and contact agents directly — all from your phone."
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = NaiCribsColors.PurpleLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📍 Based in Nairobi, Kenya", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.PurpleDark)
                        Spacer(Modifier.height(4.dp))
                        Text("Version 1.0.0", fontSize = 12.sp, color = NaiCribsColors.TextMuted, textAlign = TextAlign.Center)
                        Text("© 2025 NaiCribs. All rights reserved.", fontSize = 12.sp, color = NaiCribsColors.TextMuted, textAlign = TextAlign.Center)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AboutSection(title: String, body: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
            Text(body, fontSize = 13.sp, color = NaiCribsColors.TextMuted, lineHeight = 20.sp)
        }
    }
}