package com.example.naicribs.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.naicribs.data.AuthViewModel
import com.example.naicribs.screens.common.NaiCribsTextField
import com.example.naicribs.ui.theme.NaiCribsColors

@Composable
fun ForgotPasswordScreen(navController: NavController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaiCribsColors.Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Lock, contentDescription = null, tint = NaiCribsColors.Purple, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(16.dp))
        Text("Reset Password", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = NaiCribsColors.TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text(
            "Enter your email address and we'll send you a link to reset your password.",
            fontSize = 13.sp, color = NaiCribsColors.TextMuted, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(28.dp))

        if (state.isSuccess) {
            Surface(shape = RoundedCornerShape(12.dp), color = NaiCribsColors.TealLight, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "✅ Reset link sent! Check your inbox.",
                    modifier = Modifier.padding(14.dp), fontSize = 14.sp, color = NaiCribsColors.TealDark
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        state.errorMessage?.let {
            Surface(shape = RoundedCornerShape(10.dp), color = NaiCribsColors.ErrorLight, modifier = Modifier.fillMaxWidth()) {
                Text(it, modifier = Modifier.padding(12.dp), fontSize = 13.sp, color = NaiCribsColors.Error)
            }
            Spacer(Modifier.height(12.dp))
        }

        NaiCribsTextField(
            value = email, onValueChange = { email = it },
            label = "Email address", leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.resetPassword(email) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NaiCribsColors.Purple)
        ) {
            if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text("Send reset link", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(14.dp))
        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Back to Login", color = NaiCribsColors.Purple, fontSize = 13.sp)
        }
    }
}