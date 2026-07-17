package com.example.naicribs.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.naicribs.data.AuthViewModel
import com.example.naicribs.navigation.Routes
import com.example.naicribs.screens.common.NaiCribsTextField
import com.example.naicribs.ui.theme.NaiCribsColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    role: String = "customer"
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()


    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            val destination = when (state.currentUser?.role ?: role) {
                "agent"    -> Routes.AgentDashboard
                else       -> Routes.CustomerDashboard
            }
            navController.navigate(destination) {
                popUpTo(Routes.RoleSelection) { inclusive = true }
            }
            viewModel.clearError()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp)
                .background(Brush.verticalGradient(listOf(NaiCribsColors.PurpleDark, NaiCribsColors.Purple))),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
            ) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("NaiCribs", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    if (role == "agent") "Agent Login" else "Customer Login",
                    fontSize = 14.sp, color = Color.White.copy(alpha = 0.75f)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().background(NaiCribsColors.Background).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            Text("Sign in", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
            Text("Enter your details to continue", fontSize = 13.sp, color = NaiCribsColors.TextMuted)

            state.errorMessage?.let {
                Surface(shape = RoundedCornerShape(10.dp), color = NaiCribsColors.ErrorLight) {
                    Text(it, modifier = Modifier.padding(12.dp), fontSize = 13.sp, color = NaiCribsColors.Error)
                }
            }

            NaiCribsTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email address",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
            ) {
                IconButton(onClick = { showPass = !showPass }) {
                    Icon(
                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null, tint = NaiCribsColors.TextMuted
                    )
                }
            }

            NaiCribsTextField(
                value = password, onValueChange = { password = it },
                label = "Password", leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingContent = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null, tint = NaiCribsColors.TextMuted)
                    }
                }
            )

            Button(
                onClick = { viewModel.login(email, password) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Purple)
            ) {
                if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Sign in", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", fontSize = 13.sp, color = NaiCribsColors.TextMuted)
                Text("Register", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    color = NaiCribsColors.Purple,
                    modifier = Modifier.clickable { navController.navigate(Routes.register(role)) })
            }

            Text("Forgot password?", fontSize = 13.sp, color = NaiCribsColors.TextMuted,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                    .clickable { navController.navigate(Routes.ForgotPassword) })
        }
    }
}