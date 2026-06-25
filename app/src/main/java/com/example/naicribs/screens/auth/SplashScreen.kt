package com.example.naicribs.screens.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.naicribs.R
import com.example.naicribs.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.house_loading))
    val progress by animateLottieCompositionAsState(composition)

    LaunchedEffect(Unit) {
        delay(4000)
        navController.navigate(Routes.RoleSelection) {
            popUpTo(Routes.Splash) { inclusive = true }
        }
    }

    Box(
        modifier        = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                progress    = { progress },
                modifier    = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            GradientText(
                text     = "NaiCribs",
                gradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF7F77DD), Color(0xFF1D9E75))
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text  = "Find your next home in Nairobi",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GradientText(text: String, gradient: Brush) {
    Text(
        text      = text,
        fontSize  = 32.sp,
        fontWeight = FontWeight.Bold,
        style     = MaterialTheme.typography.headlineMedium.copy(brush = gradient)
    )
}