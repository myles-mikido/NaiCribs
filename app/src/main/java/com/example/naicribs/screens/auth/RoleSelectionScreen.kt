package com.example.naicribs.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.naicribs.navigation.Routes
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaiCribsColors.Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(NaiCribsColors.Purple, NaiCribsColors.Teal))),
            contentAlignment = Alignment.Center
        ) {
            Text("🏠", fontSize = 44.sp)
        }
        Spacer(Modifier.height(20.dp))
        Text("NaiCribs", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = NaiCribsColors.TextPrimary)
        Text(
            "Nairobi's #1 house hunting app",
            fontSize = 14.sp, color = NaiCribsColors.TextMuted,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(48.dp))
        Text("Who are you?", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
        Text("Choose to continue", fontSize = 13.sp, color = NaiCribsColors.TextMuted)
        Spacer(Modifier.height(24.dp))

        RoleCard(
            icon        = Icons.Default.Work,
            title       = "I'm an Agent",
            description = "List and manage your properties",
            color       = NaiCribsColors.Purple,
            onClick     = { navController.navigate(Routes.login("agent")) }
        )
        Spacer(Modifier.height(14.dp))
        RoleCard(
            icon        = Icons.Default.Home,
            title       = "I'm Looking for a Crib",
            description = "Browse available houses in Nairobi",
            color       = NaiCribsColors.Teal,
            onClick     = { navController.navigate(Routes.login("customer")) }
        )
    }
}

@Composable
private fun RoleCard(
    icon: ImageVector, title: String,
    description: String, color: Color, onClick: () -> Unit
) {
    Surface(
        onClick = onClick, shape = RoundedCornerShape(16.dp),
        color = color, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp)) }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text(description, fontSize = 12.sp, color = Color.White.copy(alpha = 0.75f))
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null,
                tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
        }
    }
}