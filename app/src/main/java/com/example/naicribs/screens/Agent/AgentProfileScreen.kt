package com.example.naicribs.screens.Agent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
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
import com.example.naicribs.data.AuthViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsTopBar
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun AgentProfileScreen(navController: NavController, viewModel: AuthViewModel) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { NaiCribsTopBar(title = "My Profile", onBackClick = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NaiCribsColors.Background)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.verticalGradient(listOf(NaiCribsColors.PurpleDark, NaiCribsColors.Purple))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(72.dp).clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(state.currentUser?.fullName ?: "Agent", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Agent · NaiCribs", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }

            Spacer(Modifier.height(20.dp))


            Surface(
                shape           = RoundedCornerShape(16.dp),
                color           = Color.White,
                shadowElevation = 2.dp,
                modifier        = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Account Info", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)


                    ProfileInfoRow(
                        icon  = Icons.Default.Person,
                        label = "Full name",
                        value = state.currentUser?.fullName ?: "—"
                    )
                    ProfileInfoRow(
                        icon  = Icons.Default.AccountCircle,
                        label = "Role",
                        value = "Agent"
                    )
                }
            }

            Spacer(Modifier.height(20.dp))


            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(Routes.Login) { popUpTo(0) { inclusive = true } }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Error)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Sign out", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = NaiCribsColors.Purple, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = NaiCribsColors.TextMuted)
            Text(value, fontSize = 14.sp, color = NaiCribsColors.TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}